package cd.cddt7712.inspecciones.controller;

import cd.cddt7712.inspecciones.entity.Dependency;
import cd.cddt7712.inspecciones.service.DependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dependencies")
public class DependencyController {

    @Autowired
    private DependencyService dependencyService;

    @GetMapping
    public List<Dependency> getAllDependencies() {
        return dependencyService.getAllDependencies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependency> getDependencyById(@PathVariable Long id) {
        return dependencyService.getDependencyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Dependency createDependency(@RequestBody Dependency dependency) {
        return dependencyService.createDependency(dependency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dependency> updateDependency(@PathVariable Long id, @RequestBody Dependency dependencyDetails) {
        try {
            return ResponseEntity.ok(dependencyService.updateDependency(id, dependencyDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDependency(@PathVariable Long id) {
        try {
            dependencyService.deleteDependency(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

