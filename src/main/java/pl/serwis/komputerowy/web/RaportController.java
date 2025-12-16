package pl.serwis.komputerowy.web;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.serwis.komputerowy.repository.RaportRepository;
import pl.serwis.komputerowy.service.RaportService;

@RestController
@RequestMapping("/api/raporty")
public class RaportController {

  private final RaportService raportService;

  public RaportController(RaportService raportService) {
    this.raportService = raportService;
  }

  // Zapytanie 1 z PDF
  @GetMapping("/1")
  public List<RaportRepository.PracownikStatusCount> raport1() {
    return raportService.raport1();
  }

  // Zapytanie 2 z PDF (w Postgres: to_char + extract)
  @GetMapping("/2")
  public List<RaportRepository.MiesiacCount> raport2(@RequestParam(required = false) Integer rok) {
    return raportService.raport2(rok);
  }

  // Zapytanie 3 z PDF
  @GetMapping("/3")
  public List<RaportRepository.RaportKlientaRow> raport3(@RequestParam String imie, @RequestParam String nazwisko) {
    return raportService.raport3(imie, nazwisko);
  }

  // WF.10 - eksport PDF/CSV (przykład dla raportu 1)
  @GetMapping("/1/export")
  public ResponseEntity<byte[]> exportRaport1(@RequestParam String format) {
    var rows = raportService.raport1();
    if ("csv".equalsIgnoreCase(format)) {
      byte[] payload = raportService.exportCsvRaport1(rows);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=raport1.csv")
          .contentType(new MediaType("text", "csv"))
          .body(payload);
    }
    if ("pdf".equalsIgnoreCase(format)) {
      byte[] payload = raportService.exportPdfRaport1(rows);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=raport1.pdf")
          .contentType(MediaType.APPLICATION_PDF)
          .body(payload);
    }
    return ResponseEntity.badRequest().body(("Nieznany format: " + format).getBytes());
  }
}
