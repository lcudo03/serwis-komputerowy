package pl.serwis.komputerowy.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.serwis.komputerowy.domain.*;
import pl.serwis.komputerowy.repository.*;
import pl.serwis.komputerowy.web.dto.ZlecenieCreateRequest;
import pl.serwis.komputerowy.web.error.NotFoundException;

@Service
public class ZlecenieService {

  private final ZlecenieRepository zlecenieRepository;
  private final KlientRepository klientRepository;
  private final UrzadzenieRepository urzadzenieRepository;
  private final StatusRepository statusRepository;
  private final PracownikRepository pracownikRepository;

  public ZlecenieService(
      ZlecenieRepository zlecenieRepository,
      KlientRepository klientRepository,
      UrzadzenieRepository urzadzenieRepository,
      StatusRepository statusRepository,
      PracownikRepository pracownikRepository) {
    this.zlecenieRepository = zlecenieRepository;
    this.klientRepository = klientRepository;
    this.urzadzenieRepository = urzadzenieRepository;
    this.statusRepository = statusRepository;
    this.pracownikRepository = pracownikRepository;
  }

  @Transactional
  public Zlecenie create(ZlecenieCreateRequest req) {
    Klient klient = klientRepository.findById(req.klient())
        .orElseThrow(() -> new NotFoundException("Nie znaleziono klienta id=" + req.klient()));
    Urzadzenie urzadzenie = urzadzenieRepository.findById(req.urzadzenie())
        .orElseThrow(() -> new NotFoundException("Nie znaleziono urządzenia id=" + req.urzadzenie()));

    // Front (script.js) nie wysyła statusu ani daty – ustawiamy sensowne domyślne wartości.
    Status status = null;
    if (req.status() != null) {
      status = statusRepository.findById(req.status())
          .orElseThrow(() -> new NotFoundException("Nie znaleziono statusu id=" + req.status()));
    } else {
      status = statusRepository.findByStatus("Przyjęte")
          .orElseGet(() -> statusRepository.findAll().stream().findFirst()
              .orElseThrow(() -> new NotFoundException("Brak zdefiniowanych statusów w bazie")));
    }

    Pracownik pracownik = null;
    if (req.pracownik() != null) {
      pracownik = pracownikRepository.findById(req.pracownik())
          .orElseThrow(() -> new NotFoundException("Nie znaleziono pracownika id=" + req.pracownik()));
    }

    Zlecenie z = new Zlecenie();
    z.setKlient(klient);
    z.setUrzadzenie(urzadzenie);
    z.setModelUrzadzenia(req.modelUrzadzenia());
    z.setAkcesoria(req.akcesoria());
    z.setOpisUsterki(req.opisUsterki());
    z.setData(req.data() != null ? req.data() : LocalDate.now());
    z.setStatus(status);
    z.setPracownik(pracownik);
    z.setPostepNaprawy(null); // front pokazuje 0% gdy null
    return zlecenieRepository.save(z);
  }

  @Transactional(readOnly = true)
  public Zlecenie get(long id) {
    return zlecenieRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Nie znaleziono zlecenia id=" + id));
  }

  @Transactional(readOnly = true)
  public List<Zlecenie> filter(Long statusId, Long pracownikId, LocalDate from, LocalDate to) {
    return zlecenieRepository.filter(statusId, pracownikId, from, to);
  }

  @Transactional
  public Zlecenie assignPracownik(long zlecenieId, long pracownikId) {
    Zlecenie z = get(zlecenieId);
    Pracownik p = pracownikRepository.findById(pracownikId)
        .orElseThrow(() -> new NotFoundException("Nie znaleziono pracownika id=" + pracownikId));
    z.setPracownik(p);
    return zlecenieRepository.save(z);
  }

  @Transactional
  public Zlecenie changeStatus(long zlecenieId, long statusId) {
    return changeStatus(zlecenieId, statusId, null);
  }

  @Transactional
  public Zlecenie changeStatus(long zlecenieId, long statusId, String postepNaprawy) {
    Zlecenie z = get(zlecenieId);
    Status s = statusRepository.findById(statusId)
        .orElseThrow(() -> new NotFoundException("Nie znaleziono statusu id=" + statusId));
    z.setStatus(s);
    if (postepNaprawy != null) {
      z.setPostepNaprawy(postepNaprawy);
    }
    return zlecenieRepository.save(z);
  }
}