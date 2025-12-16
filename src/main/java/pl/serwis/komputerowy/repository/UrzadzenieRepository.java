package pl.serwis.komputerowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.serwis.komputerowy.domain.Urzadzenie;

public interface UrzadzenieRepository extends JpaRepository<Urzadzenie, Long> {}
