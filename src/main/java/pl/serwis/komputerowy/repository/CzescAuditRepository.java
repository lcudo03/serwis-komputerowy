package pl.serwis.komputerowy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.serwis.komputerowy.domain.CzescAudit;

public interface CzescAuditRepository extends JpaRepository<CzescAudit, Long> {
  List<CzescAudit> findByCzescIdOrderByUtworzonoDesc(Long czescId);
}
