package cd.cddt7712.inspecciones.repository;

import cd.cddt7712.inspecciones.entity.Dependency;
import cd.cddt7712.inspecciones.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByDependency(Dependency dependency);

}
