package pl.serwis.komputerowy.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.serwis.komputerowy.domain.DoZamowienia;
import pl.serwis.komputerowy.domain.Zamowienie;
import pl.serwis.komputerowy.domain.Zlecenie;
import pl.serwis.komputerowy.repository.DoZamowieniaRepository;
import pl.serwis.komputerowy.repository.ZamowienieRepository;
import pl.serwis.komputerowy.repository.ZlecenieRepository;
import pl.serwis.komputerowy.web.dto.ZamowienieCreateRequest;
import pl.serwis.komputerowy.web.error.NotFoundException;

@Service
public class ZamowienieService {

  private final ZamowienieRepository zamRepo;
  private final ZlecenieRepository zlecenieRepo;
  private final DoZamowieniaRepository doRepo;

  public ZamowienieService(ZamowienieRepository zamRepo, ZlecenieRepository zlecenieRepo, DoZamowieniaRepository doRepo) {
    this.zamRepo = zamRepo;
    this.zlecenieRepo = zlecenieRepo;
    this.doRepo = doRepo;
  }

  @Transactional
  public Zamowienie create(ZamowienieCreateRequest req) {
    Zlecenie zlecenie = zlecenieRepo.findById(req.zlecenie())
        .orElseThrow(() -> new NotFoundException("Nie znaleziono zlecenia id=" + req.zlecenie()));
    DoZamowienia item = doRepo.findById(req.zamowionyPrzedmiot())
        .orElseThrow(() -> new NotFoundException("Nie znaleziono pozycji do_zamowienia id=" + req.zamowionyPrzedmiot()));

    Zamowienie zam = new Zamowienie();
    zam.setZlecenie(zlecenie);
    zam.setZamowionyPrzedmiot(item);
    zam.setCena(req.cena());
    zam.setOdebrane(false);
    return zamRepo.save(zam);
  }

  @Transactional(readOnly = true)
  public List<Zamowienie> filter(Boolean odebrane) {
    return zamRepo.filter(odebrane);
  }

  @Transactional
  public Zamowienie markReceived(long id, boolean received) {
    Zamowienie z = zamRepo.findById(id).orElseThrow(() -> new NotFoundException("Nie znaleziono zamówienia id=" + id));
    z.setOdebrane(received);
    return zamRepo.save(z);
  }
}
