package com.ttn.bluebell.core.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttn.bluebell.durable.model.employee.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by praveshsaini on 4/10/16.
 */
public interface EmployeeOperations {

    void findAllEmployee();

    Employee findEmployeeByEmail(String email);

    Set<Employee> findAllDeactiveEmployee();

    Employee hasExist(String email);

    Set<Employee> findEmployeesOnBench();

    Set<String> findEmployeeRoles(String email);

    Set<String> findEmployeeRegions(String email);

    Set<Employee> findAllEmployeeByCompetency(String competency, boolean requireAll, Boolean isassociateAdditionalInfo);


    List<EmployeeAuthorityDTO> findEmployeesAuthority();

    void addEmployeeAuthority(EmployeeAuthorityDTO employeeAuthorityDTO);

    void updateEmployeeRegion(String employeeEmail, String employeeNewRegion);

    Set<String> getEmployeeAllPreviousProjects(String employeeEmail);

    List<NonBillableEmployeeDTO> getAllNonBillableEmployees();
//    Map<String, Map<String, Object>> employeeOnBench(int durationInDays);

    Set<Employee> findAllEmployeeByCompetencyTitleAndSearch(EmployeeCompetencyTitleListDTO employeeCompetencyTitleListDTO);

    void getProjectsInfoForNW(Set<Employee> employees);

    void pushAllEmployeesProjectsInfo();

    void pushProjectsInfo(ProjectDetailsDTO projectDetailsDTO) throws JsonProcessingException;

}
