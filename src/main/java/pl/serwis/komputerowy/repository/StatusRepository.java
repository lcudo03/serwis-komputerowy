package pl.serwis.komputerowy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.serwis.komputerowy.domain.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
  Optional<Status> findByStatus(String status);
}
