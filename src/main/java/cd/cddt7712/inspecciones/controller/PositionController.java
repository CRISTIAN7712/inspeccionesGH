package cd.cddt7712.inspecciones.controller;

import cd.cddt7712.inspecciones.entity.Dependency;
import cd.cddt7712.inspecciones.entity.Position;
import cd.cddt7712.inspecciones.service.DependencyService;
import cd.cddt7712.inspecciones.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Autowired
    private DependencyService dependencyService;

    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        return positionService.getPositionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Position createPosition(@RequestBody Position position) {
        return positionService.createPosition(position);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @RequestBody Position positionDetails) {
        try {
            return ResponseEntity.ok(positionService.updatePosition(id, positionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        try {
            positionService.deletePosition(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dependency/{dependencyId}")
    public ResponseEntity<List<Position>> getPositionsByDependency(@PathVariable Long dependencyId) {
        Optional<Dependency> dependency = dependencyService.findById(dependencyId);

        if (dependency.isPresent()) {
            List<Position> positions = positionService.findByDependency(dependency.get());
            return new ResponseEntity<>(positions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

