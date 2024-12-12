package cd.cddt7712.inspecciones.service;

import cd.cddt7712.inspecciones.config.ResourceNotFoundException;
import cd.cddt7712.inspecciones.dto.EmployeeDTO;
import cd.cddt7712.inspecciones.dto.IdentificationTypeDTO;
import cd.cddt7712.inspecciones.entity.Employee;
import cd.cddt7712.inspecciones.entity.Selection;
import cd.cddt7712.inspecciones.repository.EmployeeRepository;
import cd.cddt7712.inspecciones.repository.SelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SelectionRepository selectionRepository;


    @Autowired
    private RestTemplate restTemplate;

    public Employee createEmployee(Employee employee) {
        // Validar cédula única
        if (employeeRepository.existsByNumberId(employee.getNumberId())) {
            throw new IllegalArgumentException("Número de cédula ya está registrado.");
        }

        // Validar correo único
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Correo electrónico ya está registrado.");
        }

        // Validar teléfono único para empleados con status 1
        if (employeeRepository.existsActiveEmployeeWithPhone(employee.getPhone())) {
            throw new IllegalArgumentException("El teléfono ya está registrado para un empleado activo.");
        }

        return employeeRepository.save(employee);
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            // Llama al microservicio para obtener el value del tipo de identificación
            String identificationTypeUrl = "http://192.168.10.224:50000/general/lv/" + employee.getIdentificationTypeId();
            IdentificationTypeDTO identificationTypeDTO;

            try {
                identificationTypeDTO = restTemplate.getForObject(identificationTypeUrl, IdentificationTypeDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Error fetching identification type value", e);
            }

            String identificationValue = identificationTypeDTO != null ? identificationTypeDTO.getValue() : null;

            // Devuelve el DTO con el valor del tipo de identificación
            return new EmployeeDTO(
                    employee.getId(),
                    employee.getFirstname(),
                    employee.getSecondname(),
                    employee.getFirstSurname(),
                    employee.getSecondSurname(),
                    identificationValue, // Incluye el value en lugar del id
                    employee.getNumberId(),
                    employee.getPosition(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getHireDate(),
                    employee.getFinalHireDate(),
                    employee.getStatus()
            );
        });
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(employee -> {
            String identificationTypeUrl = "http://192.168.10.224:50000/general/lv/" + employee.getIdentificationTypeId();
            IdentificationTypeDTO identificationTypeDTO;

            try {
                identificationTypeDTO = restTemplate.getForObject(identificationTypeUrl, IdentificationTypeDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Error fetching identification type value for employee " + employee.getId(), e);
            }

            String identificationValue = identificationTypeDTO != null ? identificationTypeDTO.getValue() : null;

            return new EmployeeDTO(
                    employee.getId(),
                    employee.getFirstname(),
                    employee.getSecondname(),
                    employee.getFirstSurname(),
                    employee.getSecondSurname(),
                    identificationValue, // Incluye el valor
                    employee.getNumberId(),
                    employee.getPosition(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getHireDate(),
                    employee.getFinalHireDate(),
                    employee.getStatus()
            );
        }).collect(Collectors.toList());
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstname(updatedEmployee.getFirstname());
            employee.setSecondname(updatedEmployee.getSecondname());
            employee.setFirstSurname(updatedEmployee.getFirstSurname());
            employee.setSecondSurname(updatedEmployee.getSecondSurname());
            employee.setIdentificationTypeId(updatedEmployee.getIdentificationTypeId());
            employee.setNumberId(updatedEmployee.getNumberId());
            employee.setPosition(updatedEmployee.getPosition());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setPhone(updatedEmployee.getPhone());
            employee.setHireDate(updatedEmployee.getHireDate());
            employee.setFinalHireDate(updatedEmployee.getFinalHireDate());
            employee.setStatus(updatedEmployee.getStatus());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Search methods for all attributes
    public List<Employee> findByFirstname(String value) {
        List<Employee> employees = employeeRepository.findByFirstnameContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with firstname containing: " + value);
        }
        return employees;
    }

    public List<Employee> findBySecondname(String value) {
        List<Employee> employees = employeeRepository.findBySecondnameContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with secondname containing: " + value);
        }
        return employees;
    }

    public List<Employee> findByFirstSurname(String value) {
        List<Employee> employees = employeeRepository.findByFirstSurnameContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with first surname containing: " + value);
        }
        return employees;
    }

    public List<Employee> findBySecondSurname(String value) {
        List<Employee> employees = employeeRepository.findBySecondSurnameContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with second surname containing: " + value);
        }
        return employees;
    }

    public List<Employee> findByIdentificationTypeId(Long identificationTypeId) {
        return employeeRepository.findByIdentificationTypeId(identificationTypeId);
    }

    public EmployeeDTO getEmployeeByNumberId(Long numberId) {
        // Busca el empleado por número de identificación
        Employee employee = employeeRepository.findByNumberId(numberId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado con número de cédula " + numberId + " no encontrado."));

        // Construir la URL para obtener el tipo de identificación
        String identificationTypeUrl = "http://192.168.10.224:50000/general/lv/" + employee.getIdentificationTypeId();
        IdentificationTypeDTO identificationTypeDTO;

        try {
            // Llamada al microservicio para obtener el valor del tipo de identificación
            identificationTypeDTO = restTemplate.getForObject(identificationTypeUrl, IdentificationTypeDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el tipo de identificación para el empleado con número de cédula " + numberId, e);
        }

        // Extrae el valor del tipo de identificación o asigna un valor por defecto
        String identificationValue = identificationTypeDTO != null ? identificationTypeDTO.getValue() : "Desconocido";

        // Convierte la entidad Employee a EmployeeDTO
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstname(),
                employee.getSecondname(),
                employee.getFirstSurname(),
                employee.getSecondSurname(),
                identificationValue, // Incluye el valor del tipo de identificación
                employee.getNumberId(),
                employee.getPosition(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                employee.getFinalHireDate(),
                employee.getStatus()
        );
    }


    public List<Employee> findByEmail(String value) {
        List<Employee> employees = employeeRepository.findByEmailContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with email containing: " + value);
        }
        return employees;
    }

    public List<Employee> findByPhone(String value) {
        List<Employee> employees = employeeRepository.findByPhoneContainingIgnoreCase(value);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with phone containing: " + value);
        }
        return employees;
    }

    public List<Employee> findByHireDate(Date hireDate) {
        return employeeRepository.findByHireDate(hireDate);
    }

    public List<Employee> findByFinalHireDate(Date finalHireDate) {
        return employeeRepository.findByFinalHireDate(finalHireDate);
    }

    public List<Employee> findByStatus(Long status) {
        return employeeRepository.findByStatus(status);
    }

    public List<Employee> findByPositionId(Long positionId) {
        List<Employee> employees = employeeRepository.findByPositionId(positionId);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No existen empleados en la posicion: " + positionId);
        }
        return employees;
    }

    public List<Employee> findEmployeesByDependency(Long dependencyId) {
        List<Employee> employees = employeeRepository.findByDependencyId(dependencyId);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No existen empleados en la dependencia: " + dependencyId);
        }
        return employees;
    }

    public List<Employee> findEmployeesByPositionAndDependency(Long positionId, Long dependencyId) {
        List<Employee> employees = employeeRepository.findByPositionAndDependency(positionId, dependencyId);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No existen empleados en el cargo: " + positionId + " y departamento: " + dependencyId);
        }
        return employees;
    }

    public List<Employee> getRandomActiveEmployees(int count) {
        List<Employee> activeEmployees = employeeRepository.findByStatus(1L); // 1L: Activo
        Collections.shuffle(activeEmployees);
        return activeEmployees.stream().limit(count).toList();
    }

    public List<Selection> saveSelections(List<Employee> employees) {
        List<Selection> selections = employees.stream()
                .map(emp -> {
                    Selection selection = new Selection();
                    selection.setEmployee(emp);
                    selection.setSelectedDate(LocalDateTime.now());
                    return selection;
                }).toList();
        return selectionRepository.saveAll(selections);
    }
}

