package pl.serwis.komputerowy.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RaportRepository {

  @PersistenceContext
  private EntityManager em;

  // DTO-y do wyników (zamiast projection interfaces)
  public record PracownikStatusCount(String pracownik, String status, long liczbaZlecen) {}
  public record MiesiacCount(String miesiac, long liczbaNapraw, int miesiacNr) {}
  public record RaportKlientaRow(String imie, String nazwisko, String urzadzenie,
                                 String modelUrzadzenia, String status, Date data, String pracownik) {}

  public List<PracownikStatusCount> raport1() {
    @SuppressWarnings("unchecked")
    List<Object[]> rows = em.createNativeQuery("""
        SELECT p.imie AS pracownik, s.status AS status, COUNT(z.id_zlecenia) AS liczbaZlecen
        FROM zlecenia z
        JOIN pracownik p ON z.pracownik = p.id_pracownika
        JOIN status s ON z.status = s.id_statusu
        GROUP BY p.imie, s.status
        ORDER BY p.imie, s.status
        """).getResultList();

    List<PracownikStatusCount> out = new ArrayList<>();
    for (Object[] r : rows) {
      out.add(new PracownikStatusCount(
          (String) r[0],
          (String) r[1],
          ((Number) r[2]).longValue()
      ));
    }
    return out;
  }

  public List<MiesiacCount> raport2(int rok) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows = em.createNativeQuery("""
        SELECT trim(to_char(z.data, 'Month')) AS miesiac,
               COUNT(z.id_zlecenia) AS liczbaNapraw,
               EXTRACT(MONTH FROM z.data)::int AS miesiacNr
        FROM zlecenia z
        WHERE EXTRACT(YEAR FROM z.data)::int = :rok
        GROUP BY EXTRACT(MONTH FROM z.data), trim(to_char(z.data, 'Month'))
        ORDER BY miesiacNr
        """)
      .setParameter("rok", rok)
      .getResultList();

    List<MiesiacCount> out = new ArrayList<>();
    for (Object[] r : rows) {
      out.add(new MiesiacCount(
          (String) r[0],
          ((Number) r[1]).longValue(),
          ((Number) r[2]).intValue()
      ));
    }
    return out;
  }

  public List<RaportKlientaRow> raport3(String imie, String nazwisko) {
    @SuppressWarnings("unchecked")
    List<Object[]> rows = em.createNativeQuery("""
        SELECT k.imie AS imie,
               k.nazwisko AS nazwisko,
               u.urzadzenie AS urzadzenie,
               z.model_urzadzenia AS modelUrzadzenia,
               s.status AS status,
               z.data AS data,
               p.imie AS pracownik
        FROM zlecenia z
        JOIN klienci k ON z.klient = k.id_klienta
        JOIN urzadzenie u ON z.urzadzenie = u.id_urzadzenia
        JOIN status s ON z.status = s.id_statusu
        LEFT JOIN pracownik p ON z.pracownik = p.id_pracownika
        WHERE lower(k.imie) = lower(:imie) AND lower(k.nazwisko) = lower(:nazwisko)
        ORDER BY z.data DESC
        """)
      .setParameter("imie", imie)
      .setParameter("nazwisko", nazwisko)
      .getResultList();

    List<RaportKlientaRow> out = new ArrayList<>();
    for (Object[] r : rows) {
      out.add(new RaportKlientaRow(
          (String) r[0],
          (String) r[1],
          (String) r[2],
          (String) r[3],
          (String) r[4],
          (Date) r[5],
          (String) r[6]
      ));
    }
    return out;
  }
}
