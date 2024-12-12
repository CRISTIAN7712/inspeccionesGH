package cd.cddt7712.inspecciones.service;

import cd.cddt7712.inspecciones.entity.Dependency;
import cd.cddt7712.inspecciones.repository.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DependencyService {

    @Autowired
    private DependencyRepository dependencyRepository;

    public List<Dependency> getAllDependencies() {
        return dependencyRepository.findAll();
    }

    public Optional<Dependency> getDependencyById(Long id) {
        return dependencyRepository.findById(id);
    }

    public Dependency createDependency(Dependency dependency) {
        return dependencyRepository.save(dependency);
    }

    public Optional<Dependency> findById(Long id) {
        return dependencyRepository.findById(id);
    }

    public Dependency updateDependency(Long id, Dependency dependencyDetails) {
        return dependencyRepository.findById(id).map(dependency -> {
            dependency.setName(dependencyDetails.getName());
            return dependencyRepository.save(dependency);
        }).orElseThrow(() -> new RuntimeException("Dependency not found with id " + id));
    }

    public void deleteDependency(Long id) {
        if (!dependencyRepository.existsById(id)) {
            throw new RuntimeException("Dependency not found with id " + id);
        }
        dependencyRepository.deleteById(id);
    }
}
