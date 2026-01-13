package pl.serwis.komputerowy.web;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.serwis.komputerowy.repository.StatusRepository;
import pl.serwis.komputerowy.web.dto.StatusDto;

@RestController
@RequestMapping("/api/statusy")
public class StatusController {

  private final StatusRepository statusRepository;

  public StatusController(StatusRepository statusRepository) {
    this.statusRepository = statusRepository;
  }

  @GetMapping
  public List<StatusDto> list() {
    return statusRepository.findAll().stream()
        .map(s -> new StatusDto(s.getId(), s.getStatus()))
        .toList();
  }
}
