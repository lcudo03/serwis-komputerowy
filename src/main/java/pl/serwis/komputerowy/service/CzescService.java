package pl.serwis.komputerowy.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.serwis.komputerowy.domain.CzescAudit;
import pl.serwis.komputerowy.domain.CzescZamienna;
import pl.serwis.komputerowy.repository.CzescAuditRepository;
import pl.serwis.komputerowy.repository.CzescZamiennaRepository;
import pl.serwis.komputerowy.web.dto.AdjustStockRequest;
import pl.serwis.komputerowy.web.dto.CzescUpsertRequest;
import pl.serwis.komputerowy.web.error.NotFoundException;

@Service
public class CzescService {

  private final CzescZamiennaRepository czescRepo;
  private final CzescAuditRepository auditRepo;

  public CzescService(CzescZamiennaRepository czescRepo, CzescAuditRepository auditRepo) {
    this.czescRepo = czescRepo;
    this.auditRepo = auditRepo;
  }

  @Transactional
  public CzescZamienna upsert(CzescUpsertRequest req) {
    CzescZamienna c = czescRepo.findByNrKatalogowy(req.nrKatalogowy()).orElseGet(CzescZamienna::new);
    c.setNazwa(req.nazwa());
    c.setNrKatalogowy(req.nrKatalogowy());
    c.setIlosc(req.ilosc());
    c.setLokalizacja(req.lokalizacja());
    return czescRepo.save(c);
  }

  @Transactional(readOnly = true)
  public List<CzescZamienna> search(String q) {
    return czescRepo.search(q);
  }

  @Transactional(readOnly = true)
  public CzescZamienna get(long id) {
    return czescRepo.findById(id).orElseThrow(() -> new NotFoundException("Nie znaleziono części id=" + id));
  }

  @Transactional
  public CzescZamienna adjustStock(long id, AdjustStockRequest req) {
    CzescZamienna c = get(id);
    int newQty = c.getIlosc() + req.zmiana();
    if (newQty < 0) {
      throw new IllegalArgumentException("Stan nie może być ujemny");
    }
    c.setIlosc(newQty);
    CzescZamienna saved = czescRepo.save(c);

    CzescAudit a = new CzescAudit();
    a.setCzesc(saved);
    a.setZmiana(req.zmiana());
    a.setPowod(req.powod());
    auditRepo.save(a);

    return saved;
  }

  @Transactional(readOnly = true)
  public List<CzescAudit> audit(long czescId) {
    return auditRepo.findByCzescIdOrderByUtworzonoDesc(czescId);
  }
}
