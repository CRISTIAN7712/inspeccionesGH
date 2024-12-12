package cd.cddt7712.inspecciones.repository;

import cd.cddt7712.inspecciones.entity.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long> {

    boolean existsByEmployeeIdAndSelectedDateBetween(Long employeeId, LocalDateTime start, LocalDateTime end);

}

