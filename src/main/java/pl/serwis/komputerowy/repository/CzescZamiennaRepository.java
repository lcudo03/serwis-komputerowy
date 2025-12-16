package pl.serwis.komputerowy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.serwis.komputerowy.domain.CzescZamienna;

public interface CzescZamiennaRepository extends JpaRepository<CzescZamienna, Long> {

  Optional<CzescZamienna> findByNrKatalogowy(String nrKatalogowy);

  @Query(value = """
      SELECT *
      FROM czesci_zamienne c
      WHERE (:q IS NULL OR :q = ''
            OR c.nazwa ILIKE ('%' || :q || '%')
            OR c.nr_katalogowy ILIKE ('%' || :q || '%'))
      ORDER BY c.nazwa
      """, nativeQuery = true)
  List<CzescZamienna> search(@Param("q") String q);
}
