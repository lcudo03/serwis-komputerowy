package pl.serwis.komputerowy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "do_zamowienia")
public class DoZamowienia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_zamowionego_przedmiotu")
  private Long id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "zamowiony_przedmiot", nullable = false, unique = true)
  private String zamowionyPrzedmiot;
}
