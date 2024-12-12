package cd.cddt7712.inspecciones.repository;

import cd.cddt7712.inspecciones.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByFirstname(String firstname);

    List<Employee> findBySecondname(String secondname);

    List<Employee> findByFirstSurname(String firstSurname);

    List<Employee> findBySecondSurname(String secondSurname);

    List<Employee> findByIdentificationTypeId(Long identificationTypeId);

    Optional<Employee> findByNumberId(Long numberId);

    List<Employee> findByEmail(String email);

    List<Employee> findByPhone(String phone);

    List<Employee> findByHireDate(Date hireDate);

    List<Employee> findByFinalHireDate(Date finalHireDate);

    List<Employee> findByStatus(Long status);

    List<Employee> findByPositionId(Long positionId);

    boolean existsByNumberId(Long numberId);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.phone = :phone AND e.status = 1")
    boolean existsActiveEmployeeWithPhone(String phone);

    // Buscar empleados por dependencia (a través de posición)
    @Query("SELECT e FROM Employee e WHERE e.position.dependency.id = :dependencyId")
    List<Employee> findByDependencyId(@Param("dependencyId") Long dependencyId);

    // Buscar empleados por posición y dependencia
    @Query("SELECT e FROM Employee e WHERE e.position.id = :positionId AND e.position.dependency.id = :dependencyId")
    List<Employee> findByPositionAndDependency(@Param("positionId") Long positionId, @Param("dependencyId") Long dependencyId);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstname) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findByFirstnameContainingIgnoreCase(@Param("value") String value);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.secondname) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findBySecondnameContainingIgnoreCase(@Param("value") String value);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstSurname) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findByFirstSurnameContainingIgnoreCase(@Param("value") String value);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.secondSurname) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findBySecondSurnameContainingIgnoreCase(@Param("value") String value);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.email) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findByEmailContainingIgnoreCase(@Param("value") String value);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.phone) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<Employee> findByPhoneContainingIgnoreCase(@Param("value") String value);

}
