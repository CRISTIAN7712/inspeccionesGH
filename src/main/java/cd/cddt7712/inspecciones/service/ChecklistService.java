package cd.cddt7712.inspecciones.service;

import cd.cddt7712.inspecciones.entity.Checklist;
import cd.cddt7712.inspecciones.entity.Employee;
import cd.cddt7712.inspecciones.repository.ChecklistRepository;
import cd.cddt7712.inspecciones.repository.EmployeeRepository;
import cd.cddt7712.inspecciones.repository.SelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final SelectionRepository selectionRepository;
    private final EmployeeRepository employeeRepository;

    public Checklist createChecklist(Long employeeId, String result, String comments) {
        // Verifica si el empleado fue seleccionado hoy
        LocalDate today = LocalDate.now();
        boolean isSelected = selectionRepository.existsByEmployeeIdAndSelectedDateBetween(
                employeeId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        if (!isSelected) {
            throw new IllegalArgumentException("El empleado no fue seleccionado para inspección hoy.");
        }

        // Busca al empleado en la base de datos
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Crea el checklist
        Checklist checklist = new Checklist();
        checklist.setEmployee(employee);
        checklist.setInspectionDate(LocalDateTime.now());
        checklist.setResult(result);
        checklist.setComments(comments);

        return checklistRepository.save(checklist);
    }

    public List<Checklist> getChecklistsByDate(LocalDate date) {
        // Usar el rango de tiempo del día dado
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // Recuperar checklists del repositorio
        return checklistRepository.findAllByInspectionDateBetween(startOfDay, endOfDay);
    }
}


