package pl.serwis.komputerowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.serwis.komputerowy.domain.Pracownik;

public interface PracownikRepository extends JpaRepository<Pracownik, Long> {}
