package com.ttn.bluebell.rest.services.common;

import com.ttn.bluebell.core.api.CommonDataOperations;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.ReportingOperations;
import com.ttn.bluebell.core.service.EmployeeTemplate;
import com.ttn.bluebell.core.service.ExcelUtility;
import com.ttn.bluebell.core.service.RegionService;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EStaffing;
import com.ttn.bluebell.durable.model.common.Currency;
import com.ttn.bluebell.durable.model.common.ProjectRegion;
import com.ttn.bluebell.durable.model.common.RegionDTO;
import com.ttn.bluebell.durable.model.common.SystemParameter;
import com.ttn.bluebell.durable.model.employee.Competency;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.employee.Role;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.repository.RegionRepository;
import com.ttn.bluebell.repository.StaffingRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ttn.bluebell.durable.model.project.billing.InvoiceInfo.InvoiceCycle;

/**
 * Created by praveshsaini on 22/9/16.
 */
@RestController
public class CommonResource {

    @Autowired
    private CommonDataOperations commonDataOperations;

    @Autowired
    private ExcelUtility excelUtility;

    @Autowired
    private EmployeeTemplate employeeTemplate;

    @Autowired
    private ReportingOperations reportingOperations;
    @Autowired
    private EmployeeOperations employeeOperations;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionService regionService;

    @RequestMapping(value = "/business-units",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public List<String> findAllBusinessUnits() {
        return commonDataOperations.findAllBusinessUnits();
    }

    @RequestMapping(value = "/regions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Set<String> findAllRegions() {
        return commonDataOperations.findAllRegions();
    }

    @RequestMapping(value = "/allregions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Set<String> fetchAllRegions() {
        return commonDataOperations.findAllActiveRegions();
    }

    @RequestMapping(value = "/regionData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ProjectRegion getRegionDataByRegionName(@RequestParam String regionName) {
        Region region = regionService.findRegion(regionName);
        return new ProjectRegion(region.getId(), region.getRegionName(), region.getExternalId());
    }

    @RequestMapping(value = "/competencies",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Set<String> findAllCompetencies() {
        Set<Employee> employees = employeeOperations.findAllEmployeeByCompetency(null, false, true);
        Set<String> employeeCompetencies = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        employees.forEach(employee -> {
            if (employee.getCompetency() != null) {
                employeeCompetencies.add(employee.getCompetency().getName());
            }
        });
        return employeeCompetencies;
    }

    @RequestMapping(value = "/currency",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Arrays.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Currency[] allCurrencies() {
        return Currency.values();
    }

    @RequestMapping(value = "/invoice-cycle",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Arrays.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public InvoiceCycle[] allInvoiceCycle() {
        return InvoiceCycle.values();
    }

    @RequestMapping(value = "/ttn/employee/title",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Set<String> allEmployeeTitles() {
        Set<Employee> employees = employeeOperations.findAllEmployeeByCompetency(null, false, false);
        Set<String> employeeTitles = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        employees.forEach(employee -> {
            employeeTitles.add(employee.getTitle());
        });
        return employeeTitles;
    }

    @RequestMapping(value = "/employee-roles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Set<String> allEmployeeRoles() {
        return Arrays.stream(Role.values()).map(role -> role.getName()).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/system-parameters/{type}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SystemParameter.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public List<SystemParameter> getExperienceParameters(@ApiParam(name = "type", value = "type", required = true) @NotNull @PathVariable String type) {
        return commonDataOperations.findEmployeeExperience(type);
    }

    @RequestMapping(value = "/competencies-rating",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Arrays.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Competency.Rating[] findAllCompetencyRating() {
        return Competency.Rating.values();
    }

    @RequestMapping(value = "/regionWiseStaffingReport",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Map.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Map<String, Map<String, Map<String, Integer>>> getRegionCompetencyDetail() {
        return reportingOperations.regionWiseCompetencyDetail();
    }

    @RequestMapping(value = "/regionWiseOpenNeeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Map.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Map<String, Map<String, Map<String, List<StaffRequest>>>> getRegionWiseOpenNeeds() {
        return reportingOperations.regionAndCompetencyWiseOpenNeedsDetail();
    }

    @RequestMapping(value = "/reviewerDataUpload",
            method = RequestMethod.POST
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public void uploadReviewerData(@ApiParam(name = "excelFile", value = "Select Excel format file and than upload") @RequestPart("file") MultipartFile excelFile) {
        try {
            excelUtility.uploadEmployeeReviewData(excelFile.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @RequestMapping(value = "/refreshCache", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public void refreshCache() {
        employeeTemplate.findAllEmployee();
    }

    @RequestMapping(value = "/project-list",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public List<String> getProjectListing(@ApiParam(name = "request", value = "request is a map field which only require two fields. eg:{'projectType':value ,regions:[list of regions]}") @RequestBody LinkedHashMap<String, Object> request) {
        return commonDataOperations.findProjectList((String) request.get("projectType"), (ArrayList<String>) request.get("regions"));
    }


}