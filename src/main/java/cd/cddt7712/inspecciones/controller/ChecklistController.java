package cd.cddt7712.inspecciones.controller;

import cd.cddt7712.inspecciones.entity.Checklist;
import cd.cddt7712.inspecciones.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @PostMapping
    public Checklist createChecklist(@RequestParam Long employeeId,
                                     @RequestParam String result,
                                     @RequestParam(required = false) String comments) {
        return checklistService.createChecklist(employeeId, result, comments);
    }

    @GetMapping
    public List<Checklist> getChecklistsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return checklistService.getChecklistsByDate(date);
    }
}
