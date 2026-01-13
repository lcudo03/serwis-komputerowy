package pl.serwis.komputerowy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.serwis.komputerowy.domain.Zlecenie;

public interface ZlecenieRepository extends JpaRepository<Zlecenie, Long> {

  @Query(value = """
      SELECT *
      FROM zlecenia z
      WHERE (:statusId IS NULL OR z.status = :statusId)
        AND (:pracownikId IS NULL OR z.pracownik = :pracownikId)
        AND (:from IS NULL OR z.data >= :from)
        AND (:to IS NULL OR z.data <= :to)
      ORDER BY z.data DESC
      """, nativeQuery = true)
  List<Zlecenie> filter(@Param("statusId") Long statusId,
                        @Param("pracownikId") Long pracownikId,
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

  /**
   * "Aktywne" zlecenia na potrzeby dashboardu: status 'Przyjęte' lub 'W realizacji'.
   */
  @Query(value = """
      SELECT z.*
      FROM zlecenia z
      JOIN status s ON z.status = s.id_statusu
      WHERE s.status IN ('Przyjęte', 'W realizacji')
      ORDER BY z.data DESC, z.id_zlecenia DESC
      """, nativeQuery = true)
  List<Zlecenie> findActiveForDashboard();

  /**
   * Ostatnio przyjęty model: najnowsze zlecenie ze statusem 'Przyjęte'.
   */
  @Query(value = """
      SELECT z.*
      FROM zlecenia z
      JOIN status s ON z.status = s.id_statusu
      WHERE s.status = 'Przyjęte'
      ORDER BY z.data DESC, z.id_zlecenia DESC
      LIMIT 1
      """, nativeQuery = true)
  List<Zlecenie> findLatestAcceptedForDashboard();
}
