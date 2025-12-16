package pl.serwis.komputerowy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "zlecenia")
public class Zlecenie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_zlecenia")
  private Long id;

  @Version
  @Column(name = "version", nullable = false)
  private long version;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "klient", nullable = false)
  private Klient klient;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "urzadzenie", nullable = false)
  private Urzadzenie urzadzenie;

  @Size(max = 120)
  @Column(name = "model_urzadzenia")
  private String modelUrzadzenia;

  @Size(max = 255)
  @Column(name = "akcesoria")
  private String akcesoria;

  @NotBlank
  @Size(max = 500)
  @Column(name = "opis_usterki", nullable = false)
  private String opisUsterki;

  @NotNull
  @Column(name = "data", nullable = false)
  private LocalDate data;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "status", nullable = false)
  private Status status;

  @Size(max = 255)
  @Column(name = "postep_naprawy")
  private String postepNaprawy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pracownik")
  private Pracownik pracownik;
}
