package pl.serwis.komputerowy.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.serwis.komputerowy.domain.Klient;
import pl.serwis.komputerowy.repository.KlientRepository;
import pl.serwis.komputerowy.web.dto.KlientCreateRequest;
import pl.serwis.komputerowy.web.error.NotFoundException;

@Service
public class KlientService {

  private final KlientRepository klientRepository;

  public KlientService(KlientRepository klientRepository) {
    this.klientRepository = klientRepository;
  }

  @Transactional
  public Klient create(KlientCreateRequest req) {
    Klient k = new Klient();
    apply(k, req);
    return klientRepository.save(k);
  }

  @Transactional
  public Klient update(long id, KlientCreateRequest req) {
    Klient k = klientRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Nie znaleziono klienta id=" + id));
    apply(k, req);
    return klientRepository.save(k);
  }

  @Transactional(readOnly = true)
  public Klient get(long id) {
    return klientRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Nie znaleziono klienta id=" + id));
  }

  @Transactional(readOnly = true)
  public List<Klient> search(String imie, String nazwisko, String telefon) {
    return klientRepository.search(imie, nazwisko, telefon);
  }

  @Transactional
  public void delete(long id) {
    if (!klientRepository.existsById(id)) {
      throw new NotFoundException("Nie znaleziono klienta id=" + id);
    }
    klientRepository.deleteById(id);
  }

  private static void apply(Klient k, KlientCreateRequest req) {
    k.setImie(req.imie());
    k.setNazwisko(req.nazwisko());
    k.setNrTelefonu(req.nrTelefonu());
    k.setAdres(req.adres());
  }
}
