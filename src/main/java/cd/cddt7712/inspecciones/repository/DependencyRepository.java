package cd.cddt7712.inspecciones.repository;

import cd.cddt7712.inspecciones.entity.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
    // Puedes agregar métodos personalizados si es necesario
}
