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
@Table(name = "pracownik")
public class Pracownik {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_pracownika")
  private Long id;

  @NotBlank
  @Size(max = 120)
  @Column(name = "imie", nullable = false)
  private String imie;
}
