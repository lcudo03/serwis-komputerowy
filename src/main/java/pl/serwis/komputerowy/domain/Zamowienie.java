package pl.serwis.komputerowy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "zamowienia")
public class Zamowienie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_zamowienia")
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "zlecenie", nullable = false)
  private Zlecenie zlecenie;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "zamowiony_przedmiot", nullable = false)
  private DoZamowienia zamowionyPrzedmiot;

  @Column(name = "cena")
  private Integer cena;

  @Column(name = "odebrane", nullable = false)
  private Boolean odebrane = false;
}
