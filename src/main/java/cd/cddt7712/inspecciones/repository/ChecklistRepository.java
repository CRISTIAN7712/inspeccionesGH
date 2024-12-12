package cd.cddt7712.inspecciones.repository;

import cd.cddt7712.inspecciones.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    List<Checklist> findAllByInspectionDateBetween(LocalDateTime start, LocalDateTime end);

}

