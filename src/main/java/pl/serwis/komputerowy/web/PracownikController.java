package pl.serwis.komputerowy.web;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.serwis.komputerowy.repository.PracownikRepository;
import pl.serwis.komputerowy.web.dto.PracownikDto;

@RestController
@RequestMapping("/api/pracownicy")
public class PracownikController {

  private final PracownikRepository pracownikRepository;

  public PracownikController(PracownikRepository pracownikRepository) {
    this.pracownikRepository = pracownikRepository;
  }

  @GetMapping
  public List<PracownikDto> list() {
    return pracownikRepository.findAll().stream()
        .map(p -> new PracownikDto(p.getId(), p.getImie()))
        .toList();
  }
}
