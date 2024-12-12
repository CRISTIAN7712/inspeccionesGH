package cd.cddt7712.inspecciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "firstname", nullable = false)
    @NotBlank(message = "Firstname is required")
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstname;

    @Column(name = "secondname")
    @Size(max = 50, message = "Secondname must not exceed 50 characters")
    private String secondname;

    @Column(name = "first_surname", nullable = false)
    @NotBlank(message = "First surname is required")
    @Size(max = 50, message = "First surname must not exceed 50 characters")
    private String firstSurname;

    @Column(name = "second_surname")
    @Size(max = 50, message = "Second surname must not exceed 50 characters")
    private String secondSurname;

    @Column(name = "identification_type_id", nullable = false)
    @NotNull(message = "Identification type ID is required")
    private Long identificationTypeId;

    @Column(name = "number_id", nullable = false, unique = true)
    @NotNull(message = "Number ID is required")
    @Digits(integer = 20, fraction = 0, message = "Number ID must be a valid number")
    private Long numberId;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    @NotNull(message = "Position is required")
    private Position position;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "hire_date", nullable = false)
    @NotNull(message = "Hire date is required")
    private Date hireDate;

    @Column(name = "final_hire_date")
    private Date finalHireDate;

    @Column(name = "status", nullable = false)
    @NotNull(message = "Status is required")
    private Long status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Ingrese un telefono valido.")
    public boolean isValidPhone() {
        if (phone == null || phone.isEmpty()) {
            return true;
        }
        return phone.matches("^\\+?[0-9]{10,15}$");
    }
}


