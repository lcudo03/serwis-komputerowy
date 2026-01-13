package pl.serwis.komputerowy.web;

import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.serwis.komputerowy.domain.Klient;
import pl.serwis.komputerowy.domain.Zlecenie;
import pl.serwis.komputerowy.repository.KlientRepository;
import pl.serwis.komputerowy.repository.ZlecenieRepository;
import pl.serwis.komputerowy.service.RaportService;
import pl.serwis.komputerowy.web.dto.KlientResponse;
import pl.serwis.komputerowy.web.dto.ZlecenieResponse;

@RestController
@RequestMapping("/api/raporty")
public class RaportController {

  private final RaportService raportService;
  private final KlientRepository klientRepository;
  private final ZlecenieRepository zlecenieRepository;

  public RaportController(RaportService raportService, KlientRepository klientRepository, ZlecenieRepository zlecenieRepository) {
    this.raportService = raportService;
    this.klientRepository = klientRepository;
    this.zlecenieRepository = zlecenieRepository;
  }

  // --- Dashboard / zakładka "Raporty" w statycznym froncie ---

  /**
   * Raport 1 (dashboard): lista klientów; front bierze .length jako "suma klientów".
   */
  @GetMapping("/1")
  public List<KlientResponse> dashboardKlienci() {
    return klientRepository.search(null, null, null).stream().map(RaportController::mapKlient).toList();
  }

  /**
   * Raport 2 (dashboard): lista "aktywnych" zleceń; front bierze .length jako "w realizacji".
   */
  @GetMapping("/2")
  public List<ZlecenieResponse> dashboardAktywneZlecenia() {
    return zlecenieRepository.findActiveForDashboard().stream().map(RaportController::mapZlecenie).toList();
  }

  /**
   * Raport 3 (dashboard): ostatni przyjęty model; front bierze r3[0].modelUrzadzenia.
   */
  @GetMapping("/3")
  public List<ZlecenieResponse> dashboardOstatniPrzyjetyModel() {
    var latestAccepted = zlecenieRepository.findLatestAcceptedForDashboard();
    if (latestAccepted != null && !latestAccepted.isEmpty()) {
      return latestAccepted.stream().map(RaportController::mapZlecenie).toList();
    }
    // Fallback: jeśli w bazie nie ma statusu "Przyjęte" lub brak takich zleceń, pokaż ostatnie zlecenie ogólnie.
    return zlecenieRepository.findAll().stream()
        .sorted(Comparator.comparing(Zlecenie::getData, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Zlecenie::getId, Comparator.nullsLast(Comparator.naturalOrder()))
            .reversed())
        .limit(1)
        .map(RaportController::mapZlecenie)
        .toList();
  }

  /**
   * Eksport CSV z wynikami per pracownik (zlecenia + rozbicie na statusy).
   * Statyczny front wywołuje dokładnie ten URL.
   */
  @GetMapping("/1/export")
  public ResponseEntity<byte[]> exportPracownicyCsv() {
    var rows = raportService.pracownikZleceniaSummary();
    byte[] payload = raportService.exportCsvPracownikZlecenia(rows);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=raport_pracownicy.csv")
        .contentType(new MediaType("text", "csv"))
        .body(payload);
  }

  // --- Legacy endpoints (z dokumentacji/PDF), zostawione żeby nic nie zniknęło ---

  @GetMapping("/legacy/1")
  public List<?> legacyRaport1() {
    return raportService.raport1();
  }

  @GetMapping("/legacy/2")
  public List<?> legacyRaport2(@RequestParam(required = false) Integer rok) {
    return raportService.raport2(rok);
  }

  @GetMapping("/legacy/3")
  public List<?> legacyRaport3(@RequestParam String imie, @RequestParam String nazwisko) {
    return raportService.raport3(imie, nazwisko);
  }

  private static KlientResponse mapKlient(Klient k) {
    return new KlientResponse(k.getId(), k.getImie(), k.getNazwisko(), k.getNrTelefonu(), k.getAdres());
  }

  private static ZlecenieResponse mapZlecenie(Zlecenie z) {
    return new ZlecenieResponse(
        z.getId(),
        z.getKlient() != null ? z.getKlient().getId() : null,
        z.getUrzadzenie() != null ? z.getUrzadzenie().getId() : null,
        z.getModelUrzadzenia(),
        z.getAkcesoria(),
        z.getOpisUsterki(),
        z.getData(),
        z.getStatus() != null ? z.getStatus().getId() : null,
        z.getPostepNaprawy(),
        z.getPracownik() != null ? z.getPracownik().getId() : null
    );
  }
}
