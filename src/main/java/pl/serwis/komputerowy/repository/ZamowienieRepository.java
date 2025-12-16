package pl.serwis.komputerowy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.serwis.komputerowy.domain.Zamowienie;

public interface ZamowienieRepository extends JpaRepository<Zamowienie, Long> {

  List<Zamowienie> findByZlecenieId(Long zlecenieId);

  @Query(value = """
      SELECT *
      FROM zamowienia z
      WHERE (:odebrane IS NULL OR z.odebrane = :odebrane)
      ORDER BY z.id_zamowienia DESC
      """, nativeQuery = true)
  List<Zamowienie> filter(@Param("odebrane") Boolean odebrane);
}
