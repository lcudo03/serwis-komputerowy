package pl.serwis.komputerowy.web;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.serwis.komputerowy.repository.UrzadzenieRepository;
import pl.serwis.komputerowy.web.dto.UrzadzenieDto;

@RestController
@RequestMapping("/api/urzadzenia")
public class UrzadzenieController {

  private final UrzadzenieRepository urzadzenieRepository;

  public UrzadzenieController(UrzadzenieRepository urzadzenieRepository) {
    this.urzadzenieRepository = urzadzenieRepository;
  }

  @GetMapping
  public List<UrzadzenieDto> list() {
    return urzadzenieRepository.findAll().stream()
        .map(u -> new UrzadzenieDto(u.getId(), u.getUrzadzenie()))
        .toList();
  }
}
