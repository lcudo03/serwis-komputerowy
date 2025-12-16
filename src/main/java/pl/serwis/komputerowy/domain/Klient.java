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
@Table(name = "klienci")
public class Klient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_klienta")
  private Long id;

  @NotBlank
  @Size(max = 120)
  @Column(name = "imie", nullable = false)
  private String imie;

  @NotBlank
  @Size(max = 120)
  @Column(name = "nazwisko", nullable = false)
  private String nazwisko;

  @Size(max = 50)
  @Column(name = "nr_telefonu")
  private String nrTelefonu;

  @Size(max = 255)
  @Column(name = "adres")
  private String adres;
}
