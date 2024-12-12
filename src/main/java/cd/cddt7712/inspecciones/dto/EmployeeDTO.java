package cd.cddt7712.inspecciones.dto;

import cd.cddt7712.inspecciones.entity.Position;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstname;
    private String secondname;
    private String firstSurname;
    private String secondSurname;
    private String identificationTypeValue; // Cambiado para almacenar el value
    private Long numberId;
    private Position position;
    private String email;
    private String phone;
    private Date hireDate;
    private Date finalHireDate;
    private Long status;

    public EmployeeDTO(Long id, String firstname, String secondname, String firstSurname, String secondSurname,
                       String identificationTypeValue, Long numberId, Position position, String email, String phone,
                       Date hireDate, Date finalHireDate, Long status) {
        this.id = id;
        this.firstname = firstname;
        this.secondname = secondname;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.identificationTypeValue = identificationTypeValue;
        this.numberId = numberId;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.finalHireDate = finalHireDate;
        this.status = status;
    }

    // Getters y setters
}
