package pl.serwis.komputerowy.web;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.serwis.komputerowy.repository.DoZamowieniaRepository;
import pl.serwis.komputerowy.web.dto.DoZamowieniaDto;

@RestController
@RequestMapping("/api/do-zamowienia")
public class DoZamowieniaController {

  private final DoZamowieniaRepository doZamowieniaRepository;

  public DoZamowieniaController(DoZamowieniaRepository doZamowieniaRepository) {
    this.doZamowieniaRepository = doZamowieniaRepository;
  }

  @GetMapping
  public List<DoZamowieniaDto> list() {
    return doZamowieniaRepository.findAll().stream()
        .map(d -> new DoZamowieniaDto(d.getId(), d.getZamowionyPrzedmiot()))
        .toList();
  }
}
