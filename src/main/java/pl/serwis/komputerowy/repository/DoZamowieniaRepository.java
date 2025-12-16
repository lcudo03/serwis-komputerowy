package pl.serwis.komputerowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.serwis.komputerowy.domain.DoZamowienia;

public interface DoZamowieniaRepository extends JpaRepository<DoZamowienia, Long> {}
