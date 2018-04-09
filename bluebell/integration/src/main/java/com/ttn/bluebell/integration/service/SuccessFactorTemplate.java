package com.ttn.bluebell.integration.service;

import com.ttn.bluebell.durable.model.common.RegionDTO;
import com.ttn.bluebell.durable.model.common.RegionListDTO;
import com.ttn.bluebell.durable.model.employee.*;
import com.ttn.bluebell.integration.api.SuccessFactorOperations;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by praveshsaini on 22/9/16.
 */
@Service
public class SuccessFactorTemplate implements SuccessFactorOperations {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SuccessFactorTemplate.class);

    @Override
    public List<String> findAllBusinessUnits() {
        String uri = "http://success-factors.tothenew.com/dataIntegration/fetchBusinessUnit?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> object = (LinkedHashMap<String, Object>) restTemplate.getForObject(uri, Object.class);
        List<String> businessUnits = ((LinkedHashMap<String, ArrayList<String>>) object.get("data")).get("serviceLines");
        return businessUnits;
    }

    @Override
    public Set<String> findAllRegions() {
        return StringUtils.commaDelimitedListToSet("US East,US West,US Central + ANZ,ME,India,Video,EU,Smart TV,SEA,HDFC,Corp,GIH,NAC");
    }

    @Override
    public List<RegionDTO> fetchAllRegions() {
        String url = "http://success-factors.tothenew.com/api/core/region?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RegionListDTO> responseEntity = null;
        List<RegionDTO> response = new ArrayList<>();
        try {
            responseEntity = restTemplate.exchange
                    (url, HttpMethod.GET, null, new ParameterizedTypeReference<RegionListDTO>() {
                    });

            response.addAll(responseEntity.getBody().getData());
        } catch (Exception ignored) {

        }
        return response.size() > 0 ? response : null;
    }

    @Override
    public Set<String> findAllCompetencies() {
        String uri = "http://success-factors.tothenew.com/DataIntegration/fetchAllCompetencies?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        CompetencyDTO compDTO = restTemplate.getForObject(uri, CompetencyDTO.class);
        List<CompetencyDTOInner> list = compDTO.getCompetencies();
        Set<String> dependencies = new HashSet<>();
        list.forEach(comp -> dependencies.add(comp.getName()));
        return dependencies;
    }

    @Override
    public List<String> findAllEmployeeTitles() {
        String uri = "http://success-factors.tothenew.com/dataIntegration/fetchJobTitleList?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap<String, Object> object = (LinkedHashMap<String, Object>) restTemplate.getForObject(uri, Object.class);
        List<String> employeeTitles = ((LinkedHashMap<String, ArrayList<String>>) object.get("success")).get("jobTitles");
        return employeeTitles;
    }

    @Override
    public Set<Employee> findAllEmployee() {
        String uri = "http://success-factors.tothenew.com/dataIntegration/exportData?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        EmployeeListDTO employeeListDTO = restTemplate.getForObject(uri, EmployeeListDTO.class);
        Set<Employee> employees = new HashSet<>();
        Random r = new Random();
        employeeListDTO.getUser().forEach(employeeDetailsDTO -> {
            if (employeeDetailsDTO.isTrackable() && !employeeDetailsDTO.getDesignation().equals("INTERN - TECHNOLOGY")) {
                Employee employee = new Employee();
                employee.setCode(employeeDetailsDTO.getEmployeeId());
                employee.setJoiningDate(employeeDetailsDTO.getDateOfJoining());
                employee.setEmailAddress(employeeDetailsDTO.getUsername());
                employee.setTitle(employeeDetailsDTO.getDesignation());
                employee.setImageUrl(employeeDetailsDTO.getProfilePicUrl());
                employee.setName(employeeDetailsDTO.getName());
                employee.setNoOfYearsExp(r.nextInt(20));
                employee.setFunction(employeeDetailsDTO.getFunction());
                employee.setBusinessUnit(employeeDetailsDTO.getBusinessUnit());
                employee.setDepartmentOfEmployee(employeeDetailsDTO.getDepartmentOfEmployee());
                employee.setMentovisor(employeeDetailsDTO.getSupervisor());
                if (!StringUtils.isEmpty(employeeDetailsDTO.getCompetency())) {
                    Competency competency = new Competency();
                    competency.setName(employeeDetailsDTO.getCompetency());
                    employee.setCompetency(competency);
                }
                employees.add(employee);
            }
        });
        return employees;
    }

    @Override
    public Set<Employee> findAllDeactiveEmployee() {
        String uri = "http://success-factors.tothenew.com/dataIntegration/exportData?uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        EmployeeListDTO employeeListDTO = restTemplate.getForObject(uri, EmployeeListDTO.class);
        Set<Employee> employees = new HashSet<>();
        Random r = new Random();
        employeeListDTO.getUser().forEach(employeeDetailsDTO -> {
            if (!employeeDetailsDTO.isTrackable() && !employeeDetailsDTO.getDesignation().equals("INTERN - TECHNOLOGY")) {
                Employee employee = new Employee();
                employee.setCode(employeeDetailsDTO.getEmployeeId());
                employee.setJoiningDate(employeeDetailsDTO.getDateOfJoining());
                employee.setEmailAddress(employeeDetailsDTO.getUsername());
                employee.setTitle(employeeDetailsDTO.getDesignation());
                employee.setImageUrl(employeeDetailsDTO.getProfilePicUrl());
                employee.setName(employeeDetailsDTO.getName());
                employee.setNoOfYearsExp(r.nextInt(20));
                employee.setFunction(employeeDetailsDTO.getFunction());
                employee.setBusinessUnit(employeeDetailsDTO.getBusinessUnit());
                employee.setDepartmentOfEmployee(employeeDetailsDTO.getDepartmentOfEmployee());
                employee.setMentovisor(employeeDetailsDTO.getSupervisor());
                if (!StringUtils.isEmpty(employeeDetailsDTO.getCompetency())) {
                    Competency competency = new Competency();
                    competency.setName(employeeDetailsDTO.getCompetency());
                    employee.setCompetency(competency);
                }
                employees.add(employee);
            }
        });
        return employees;
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        String uri = "http://success-factors.tothenew.com/dataIntegration/fetchUserDetail?username=" + email + "&uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
        RestTemplate restTemplate = new RestTemplate();
        EmployeeDTO employeeDTO = restTemplate.getForObject(uri, EmployeeDTO.class);
        Employee employee = EmployeeDTO.fromDTO(employeeDTO);
        employee.setEmailAddress(email);
        return employee;
    }

    @Override
    public Employee findRegionHeadByRegion(String Region) {
        return null;
    }

//    @Override
//    public List<ResignationDTO> findAllResignations() {
//        String url =  "http://sf-uat.qa2.tothenew.net/DataIntegration/fetchResignedUserDetailsForStaffing?$format=JSON&uuid=5794d117-73d7-4692-906a-6fb1fdee06e2";
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<List<ResignationDTO>> responseEntity=null;
//        List<ResignationDTO> response=null;
//        try {
//            responseEntity = restTemplate.exchange
//                    (url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResignationDTO>>() {
//                    });
//
//           response = responseEntity.getBody();
//        } catch (Exception ignored) {
//
//        }
//       return response;
//    }

    @CacheEvict(cacheNames = "employees", allEntries = true, beforeInvocation = true)
    public void evictEmployeeCache() {
    }

}
