package pl.serwis.komputerowy.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "czesc_audit")
public class CzescAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_audytu")
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "czesc_id", nullable = false)
  private CzescZamienna czesc;

  @Column(name = "zmiana", nullable = false)
  private int zmiana;

  @Size(max = 255)
  @Column(name = "powod")
  private String powod;

  @Column(name = "utworzono", nullable = false, updatable = false)
  private Instant utworzono;

  @PrePersist
  void onCreate() {
    this.utworzono = Instant.now();
  }
}
