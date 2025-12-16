package pl.serwis.komputerowy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "czesci_zamienne",
    uniqueConstraints = {@UniqueConstraint(name = "uk_czesc_nr_katalogowy", columnNames = {"nr_katalogowy"})})
public class CzescZamienna extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_czesci")
  private Long id;

  @Version
  @Column(name = "version", nullable = false)
  private long version;

  @NotBlank
  @Size(max = 255)
  @Column(name = "nazwa", nullable = false)
  private String nazwa;

  @NotBlank
  @Size(max = 120)
  @Column(name = "nr_katalogowy", nullable = false)
  private String nrKatalogowy;

  @Min(0)
  @Column(name = "ilosc", nullable = false)
  private int ilosc;

  @Size(max = 120)
  @Column(name = "lokalizacja")
  private String lokalizacja;
}
