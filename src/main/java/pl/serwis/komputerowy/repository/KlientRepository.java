package pl.serwis.komputerowy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.serwis.komputerowy.domain.Klient;

public interface KlientRepository extends JpaRepository<Klient, Long> {
    @Query("""
    select k from Klient k
    where (:imie is null or :imie = '' or lower(k.imie) like lower(concat('%', :imie, '%')))
      and (:nazwisko is null or :nazwisko = '' or lower(k.nazwisko) like lower(concat('%', :nazwisko, '%')))
      and (:telefon is null or :telefon = '' or k.nrTelefonu = :telefon)
    order by k.nazwisko, k.imie
    """)
    List<Klient> search(
            @Param("imie") String imie,
            @Param("nazwisko") String nazwisko,
            @Param("telefon") String telefon
    );

}
