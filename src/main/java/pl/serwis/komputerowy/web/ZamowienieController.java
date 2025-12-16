package pl.serwis.komputerowy.web;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import pl.serwis.komputerowy.domain.Zamowienie;
import pl.serwis.komputerowy.service.ZamowienieService;
import pl.serwis.komputerowy.web.dto.ZamowienieCreateRequest;
import pl.serwis.komputerowy.web.dto.ZamowienieResponse;

@RestController
@RequestMapping("/api/zamowienia")
public class ZamowienieController {

  private final ZamowienieService zamowienieService;

  public ZamowienieController(ZamowienieService zamowienieService) {
    this.zamowienieService = zamowienieService;
  }

  // UC7 - tworzenie zamówień części
  @PostMapping
  public ZamowienieResponse create(@Valid @RequestBody ZamowienieCreateRequest req) {
    return map(zamowienieService.create(req));
  }

  @GetMapping
  public List<ZamowienieResponse> list(@RequestParam(required = false) Boolean odebrane) {
    return zamowienieService.filter(odebrane).stream().map(ZamowienieController::map).toList();
  }

  @PatchMapping("/{id}/odebrane")
  public ZamowienieResponse markReceived(@PathVariable long id, @RequestParam boolean value) {
    return map(zamowienieService.markReceived(id, value));
  }

  private static ZamowienieResponse map(Zamowienie z) {
    return new ZamowienieResponse(
        z.getId(),
        z.getZlecenie().getId(),
        z.getZamowionyPrzedmiot().getId(),
        z.getCena(),
        z.getOdebrane()
    );
  }
}
