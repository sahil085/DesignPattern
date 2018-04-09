package com.ttn.bluebell.integration.api;


import com.ttn.bluebell.durable.model.common.RegionDTO;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.ResignationDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by praveshsaini on 22/9/16.
 */
public interface SuccessFactorOperations {

    List<String> findAllBusinessUnits();

    Set<String> findAllRegions();

    List<RegionDTO> fetchAllRegions();

    Set<String> findAllCompetencies();

    List<String> findAllEmployeeTitles();

    Set<Employee> findAllEmployee();

    Set<Employee> findAllDeactiveEmployee();

    Employee findEmployeeByEmail(String email);

    Employee findRegionHeadByRegion(String Region);
//    List<ResignationDTO> findAllResignations();
}
