package com.ttn.bluebell.repository.config;

import com.ttn.bluebell.domain.employee.EEmployeeRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ttn on 19/10/16.
 */
public interface EmployeeRoleRepository extends JpaRepository<EEmployeeRoles,Long> {

    List<EEmployeeRoles> findByEmail(String email);
    /*@Query(value = "select email from EMPLOYEE_ROLES where email=:email", nativeQuery = true)
    List<String> findEmployeeGroupByEmail(@Param("email") String email);
*/
    void deleteByEmail( String email);


}
