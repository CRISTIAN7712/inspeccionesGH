package cd.cddt7712.inspecciones.controller;

import cd.cddt7712.inspecciones.dto.EmployeeDTO;
import cd.cddt7712.inspecciones.entity.Employee;
import cd.cddt7712.inspecciones.repository.EmployeeRepository;
import cd.cddt7712.inspecciones.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private  EmployeeRepository employeeRepository;


    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody @Valid Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints for search by attributes
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String attribute, @RequestParam String value) {
        List<Employee> result;
        switch (attribute) {
            case "firstname": result = employeeService.findByFirstname(value); break;
            case "secondname": result = employeeService.findBySecondname(value); break;
            case "firstSurname": result = employeeService.findByFirstSurname(value); break;
            case "secondSurname": result = employeeService.findBySecondSurname(value); break;
            case "email": result = employeeService.findByEmail(value); break;
            case "phone": result = employeeService.findByPhone(value); break;
            default: throw new IllegalArgumentException("Unsupported search attribute: " + attribute);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dni")
    public ResponseEntity<?> getEmployeeByNumberId(@RequestParam Long numberId) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeByNumberId(numberId);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/position/{positionId}")
    public List<Employee> getEmployeesByPosition(@PathVariable Long positionId) {
        return employeeService.findByPositionId(positionId);
    }

    @GetMapping("/dependency/{dependencyId}")
    public List<Employee> getEmployeesByDependency(@PathVariable Long dependencyId) {
        return employeeService.findEmployeesByDependency(dependencyId);
    }

    @GetMapping("/dependencyandposition")
    public List<Employee> getEmployeesByPositionAndDependency(
            @RequestParam Long positionId,
            @RequestParam Long dependencyId) {
        return employeeService.findEmployeesByPositionAndDependency(positionId, dependencyId);
    }

    @GetMapping("/active/random")
    public List<Employee> getRandomEmployees(@RequestParam int count) {
        return employeeService.getRandomActiveEmployees(count);
    }
}