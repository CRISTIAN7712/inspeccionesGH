package cd.cddt7712.inspecciones.service;

import cd.cddt7712.inspecciones.entity.Dependency;
import cd.cddt7712.inspecciones.entity.Position;
import cd.cddt7712.inspecciones.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Optional<Position> getPositionById(Long id) {
        return positionRepository.findById(id);
    }

    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    public Position updatePosition(Long id, Position positionDetails) {
        return positionRepository.findById(id).map(position -> {
            position.setName(positionDetails.getName());
            position.setDependency(positionDetails.getDependency());
            return positionRepository.save(position);
        }).orElseThrow(() -> new RuntimeException("Position not found with id " + id));
    }

    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found with id " + id);
        }
        positionRepository.deleteById(id);
    }

    // Buscar Position por Dependency
    public List<Position> findByDependency(Dependency dependency) {
        return positionRepository.findByDependency(dependency);
    }
}
