package com.ttn.bluebell.rest.services.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.StaffingOperations;
import com.ttn.bluebell.core.service.CommonDataTemplate;
import com.ttn.bluebell.core.service.EmployeeTemplate;
import com.ttn.bluebell.core.service.MentovisorService;
import com.ttn.bluebell.core.service.ResignationService;
import com.ttn.bluebell.domain.employee.EEmployeeRegions;
import com.ttn.bluebell.domain.employee.EmployeeResignation;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.durable.model.common.BBResponse;
import com.ttn.bluebell.durable.model.employee.*;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.staffing.AllocationDetails;
import com.ttn.bluebell.integration.service.NewersWorldTemplate;
import com.ttn.bluebell.repository.EmployeeRegionRepository;
import com.ttn.bluebell.repository.EmployeeResignationRepository;
import com.ttn.bluebell.rest.services.common.AuthenticatorResourceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by praveshsaini on 4/10/16.
 */
@RestController
@RequestMapping("/ttn/employee")
public class EmployeeResource {

    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private EmployeeRegionRepository employeeRegionRepository;
    @Autowired
    private StaffingOperations staffingOperations;
    @Autowired
    private EmployeeResignationRepository resignationRepository;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private CommonDataTemplate commonDataTemplate;
    @Autowired
    private MentovisorService mentovisorService;
    @Autowired
    private EmployeeTemplate employeeTemplate;
    @Autowired
    private NewersWorldTemplate newersWorldTemplate;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "competency", value = "JVM", required = false, dataType = "string", paramType = "query", defaultValue = " "),
            @ApiImplicitParam(name = "requireAll", value = "Boolean value", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Employee.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Set<Employee> allEmployees(@RequestParam(required = false) String competency, @RequestParam(required = false) boolean requireAll) {
        return employeeOperations.findAllEmployeeByCompetency(competency, requireAll, false);
    }

    @RequestMapping(
            value = "/getAllEmail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Map<String, Region> getAllEmployeesEmailWithRegion() {
        return employeeTemplate.getAllEmployeesEmailWithRegion();
    }

    @RequestMapping(value = "/findOne",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EmployeeAuthorityDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public EmployeeAuthorityDTO findOne(@ApiParam(hidden = true) HttpServletRequest request) {
        PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = (PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue();
        String email = (String) preAuthenticatedAuthenticationToken.getCredentials();
        return new EmployeeAuthorityDTO(email, employeeOperations.findEmployeeByEmail(email), employeeOperations.findEmployeeRegions(email), employeeOperations.findEmployeeRoles(email));
    }
    //TODO need to handell the code for refreshing cache

    @RequestMapping(value = "/getRoles",

            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "web", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "email", paramType = "query", value = "Employee email eg:abc@tothenew.com ", required = false, dataType = "string"),
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})


    public Set<String> getRoles(@RequestParam String email) {
        return employeeOperations.findEmployeeRoles(email);
    }

    @RequestMapping(value = "/authority",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EmployeeAuthorityDTO.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    public List<EmployeeAuthorityDTO> getAllEmployeeAuthority() {
        return employeeOperations.findEmployeesAuthority();
    }

    @RequestMapping(value = "/authority",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    public void getAllEmployeeAuthority(@ApiParam @RequestBody @Valid EmployeeAuthorityDTO employeeAuthorityDTO) {
        employeeOperations.addEmployeeAuthority(employeeAuthorityDTO);
    }

    @RequestMapping(value = "/bench",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Employee.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    public Set<Employee> getAllEmployeesOnBench() {
        return employeeOperations.findEmployeesOnBench();
    }

    @RequestMapping(value = "/allocation",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Set<AllocationDetails> getEmployeeAllocations(@ApiParam(name = "params", value = "Only needs 'emailId' as a key and employee email as value") @RequestBody LinkedHashMap<String, Object> params) {
        return staffingOperations.getAllocationForEmail((String) params.get("emailId"));
    }

    @RequestMapping(value = "/partiallybench", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Map.class, responseContainer = "Map<String,Object>"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "durationInDays", example = "abcdcd", value = "durationInDays is the integer value which will be added to the current date", paramType = "query", dataType = "int")})

    public Map<String, Map<String, Object>> getAllEmployeeOnBench(@RequestParam int durationInDays) {
        return commonDataTemplate.employeeOnBench(durationInDays);
    }

    @RequestMapping(value = "/region",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Map"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "email", example = "abc@tothenew.com", value = "Employee email", paramType = "query", dataType = "string")})


    public Map<String, String> getEmployeeRegion(@RequestParam String email) {
        Map<String, String> region = new HashMap<String, String>();
        List<EEmployeeRegions> employeeRegions = employeeRegionRepository.findByEmail(email);
        if (employeeRegions.size() > 0) {
            region.put("region", employeeRegions.stream().findFirst().get().getRegion());
        }

        return region;
    }

    @RequestMapping(value = "/resignations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = EmployeeResignation.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    public List<EmployeeResignation> findAllResignations() {
        return resignationRepository.findAll();
    }

    @RequestMapping(value = "/resignations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    public void resignationApplied(@RequestBody @Valid ResignationDTO resignation) {
        resignationService.saveEmployeeResignation(resignation);
    }

    @RequestMapping(value = "/resignations",
            method = RequestMethod.DELETE
    )


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "empCode", paramType = "query", required = true, value = "Employee Code", dataType = "string"),
    })
    public void cancelResignation(@RequestParam String empCode) {

        resignationService.deleteEmployeeResignation(empCode);

    }


    @RequestMapping(value = "/getAllEmployeesMentovisor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MentovisorDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "empCode", paramType = "query", required = true, value = "Employee Code", dataType = "long"),
    })
    public List<MentovisorDTO> getAllEmployeesMentovisor() {
        return mentovisorService.getAllEmployeesMentovisor();
    }

    @RequestMapping(value = "/getmentovisor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MentovisorDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string"),
            @ApiImplicitParam(name = "empCode", paramType = "query", required = true, value = "Employee Code", dataType = "long"),
    })
    public MentovisorDTO getMentovisor(@RequestParam Long empCode) {
        return mentovisorService.getMentovisor(empCode);
    }

    @RequestMapping(value = "/changementovisor",
            method = RequestMethod.PUT

    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})
    public Employee changeMentovisor(@ApiParam @Validated @RequestBody ChangeMentovisorDTO changeMentovisorDTO) throws IOException, TimeoutException {
        Employee employee = mentovisorService.changeMentovisor(changeMentovisorDTO);
        mentovisorService.publishEvent(changeMentovisorDTO);
        return employee;
    }

    @RequestMapping(value = "/NonBillable",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})
    public List<NonBillableEmployeeDTO> getAllNonBillableEmployees() {
        return employeeOperations.getAllNonBillableEmployees();
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Employee.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    public Set<Employee> allEmployeesByCompentencyTitleAndSearch(@RequestBody@Valid EmployeeCompetencyTitleListDTO employeeCompetencyTitleListDTO) {
        return employeeOperations.findAllEmployeeByCompetencyTitleAndSearch(employeeCompetencyTitleListDTO);
    }

    @RequestMapping(
            value = "/competency/bench",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public List<CompetencyBenchDTO> getAllCompetencyBench() {
        return employeeTemplate.getAllEmployeesWithCompetencyBench();
    }

    @RequestMapping(
            value = "/leavesCount",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")})

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Map<String,Integer> leavesUpdate() {
        return newersWorldTemplate.refreshEmployeesLeaveCounts();
    }

    @RequestMapping(
            value = "/leavesSummary",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "n", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "username", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public String leavesSummary(@RequestParam Integer n, @RequestParam String username) {
        return newersWorldTemplate.getLeaveSummary(n,username);
    }

    @RequestMapping(
            value = "/pushAllUsersProjectInfo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public void pushProjectsInfoToNW() {
        employeeTemplate.pushAllEmployeesProjectsInfo();
    }

}