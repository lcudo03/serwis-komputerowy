package pl.serwis.komputerowy.web;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import pl.serwis.komputerowy.domain.Klient;
import pl.serwis.komputerowy.service.KlientService;
import pl.serwis.komputerowy.web.dto.KlientCreateRequest;
import pl.serwis.komputerowy.web.dto.KlientResponse;

@RestController
@RequestMapping("/api/klienci")
public class KlientController {

  private final KlientService klientService;

  public KlientController(KlientService klientService) {
    this.klientService = klientService;
  }

  @PostMapping
  public KlientResponse create(@Valid @RequestBody KlientCreateRequest req) {
    return map(klientService.create(req));
  }

  @PutMapping("/{id}")
  public KlientResponse update(@PathVariable long id, @Valid @RequestBody KlientCreateRequest req) {
    return map(klientService.update(id, req));
  }

  @GetMapping("/{id}")
  public KlientResponse get(@PathVariable long id) {
    return map(klientService.get(id));
  }

  @GetMapping
  public List<KlientResponse> list(@RequestParam(required = false) String imie,
                                   @RequestParam(required = false) String nazwisko,
                                   @RequestParam(required = false) String telefon) {
    return klientService.search(imie, nazwisko, telefon).stream().map(KlientController::map).toList();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) {
    klientService.delete(id);
  }

  private static KlientResponse map(Klient k) {
    return new KlientResponse(k.getId(), k.getImie(), k.getNazwisko(), k.getNrTelefonu(), k.getAdres());
  }
}
