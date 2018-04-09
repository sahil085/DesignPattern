package com.ttn.bluebell.rest.services.staffing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttn.bluebell.core.api.EmployeeOperations;
import com.ttn.bluebell.core.api.StaffingOperations;
import com.ttn.bluebell.core.scheduler.SchedulerService;
import com.ttn.bluebell.core.service.MentovisorService;
import com.ttn.bluebell.core.service.ReportingService;
import com.ttn.bluebell.durable.model.employee.UpcomingDeallocationDTO;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.staffing.Staffing;
import com.ttn.bluebell.rest.services.common.AuthenticatorResourceService;
import com.ttn.bluebell.rest.services.project.ProjectResource;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by praveshsaini on 27/9/16.
 */
@RestController
@RequestMapping("/project")
public class StaffingResource {

    @Autowired
    private StaffingOperations staffingOperations;

    @Autowired
    private ProjectResource projectResource;

    @Autowired
    private ReportingService reportingService;
    @Autowired
    private EmployeeOperations employeeOperations;
    @Autowired
    private MentovisorService mentovisorService;
    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping(value = "/{projectId}/staffing-request",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = StaffRequest.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public StaffRequest updateProjectStaffingRequest(@ApiParam(hidden = true) HttpServletRequest request,
                                                     @ApiParam(name = "projectId", required = true) @NotNull @PathVariable Long projectId,
                                                     @ApiParam @Valid @RequestBody StaffRequest staffRequest) {
        staffRequest = staffingOperations.updateStaffingRequest(projectId, staffRequest, ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));
        staffRequest=getNominations(staffRequest);
        return staffRequest;


    }
    @RequestMapping(value = "/{projectId}/staffing-request",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = StaffRequest.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public StaffRequest createProjectStaffingRequest(@ApiParam(hidden = true) HttpServletRequest request,
                                                     @ApiParam(name = "projectId", required = true) @NotNull @PathVariable Long projectId,
                                                     @ApiParam @Valid @RequestBody StaffRequest staffRequest) {
        staffRequest=staffingOperations.updateStaffingRequest(projectId, staffRequest, ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));
        staffRequest=getNominations(staffRequest);
        return staffRequest;


    }

    private StaffRequest getNominations(StaffRequest staffRequest)
    {   List<Staffing> nominations = staffingOperations.validStaffing(staffRequest, Staffing.State.Nominated);
        if (!nominations.isEmpty()) {
            nominations.forEach(nomination -> {
                nomination.setEmployeeCode(employeeOperations.findEmployeeByEmail(nomination.getEmail()).getCode());
                nomination.setMentovisor(mentovisorService.getMentovisor(Long.parseLong(nomination.getEmployeeCode())));
            });

            staffRequest.setNominatedStaffings(nominations);
            List<Staffing> allocation = staffingOperations.validStaffing(staffRequest, Staffing.State.Allocated);
            if (!allocation.isEmpty()) {
                staffRequest.setAllocatedStaffing(allocation.get(allocation.size() - 1));
                allocation.forEach(staffing -> {
                    staffing.setEmployeeCode(employeeOperations.findEmployeeByEmail(staffing.getEmail()).getCode());
                    staffing.setMentovisor(mentovisorService.getMentovisor(Long.parseLong(staffing.getEmployeeCode())));
                });
            }

        }

        return staffRequest;
    }
    @RequestMapping(value = "/{projectId}/staffing-request/{staffId}",
            method = RequestMethod.DELETE
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
    public void deleteProjectStaffingRequest(@ApiParam(name = "projectId", value = "Project Id", required = true) @NotNull @PathVariable Long projectId, @ApiParam(name = "staffId", value = "Staff Id", required = true) @NotNull @PathVariable Long staffId) {
        staffingOperations.delete(projectId, staffId);

    }

    @RequestMapping(value = "/{projectId}/staffing-request",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = StaffRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public List<StaffRequest> getProjectStaffingRequest(@ApiParam(name = "projectId", value = "Project ID", required = true) @NotNull @PathVariable Long projectId) {

        Project project = projectResource.findProject(projectId);
        List<StaffRequest> staffRequests = project.getStaffRequests();
        for (StaffRequest staffRequest : staffRequests) {
            List<Staffing> nominations = staffingOperations.validStaffing(staffRequest, Staffing.State.Nominated);
            if (!nominations.isEmpty() && nominations != null) {
                nominations.forEach(nomination -> {
                    nomination.setEmployeeCode(employeeOperations.findEmployeeByEmail(nomination.getEmail()).getCode());
                    nomination.setMentovisor(mentovisorService.getMentovisor(Long.parseLong(nomination.getEmployeeCode())));
                });
            }
            staffRequest.setNominatedStaffings(nominations);
            List<Staffing> allocation = staffingOperations.validStaffing(staffRequest, Staffing.State.Allocated);
            if (!allocation.isEmpty() && allocation != null) {
                staffRequest.setAllocatedStaffing(allocation.get(allocation.size() - 1));
                allocation.forEach(staffing -> {
                    staffing.setEmployeeCode(employeeOperations.findEmployeeByEmail(staffing.getEmail()).getCode());
                    staffing.setMentovisor(mentovisorService.getMentovisor(Long.parseLong(staffing.getEmployeeCode())));
                });
            }

        }
        staffRequests = staffRequests.stream().filter(StaffRequest::getActive).collect(Collectors.toList());
        return staffRequests;
    }

    @RequestMapping(value = "/{projectId}/staffing-request/{staffingRequestId}/allocate/{email}",
            method = RequestMethod.PUT
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

    public void allocateStaff(@ApiParam(hidden = true) HttpServletRequest request,
                              @ApiParam(name = "projectId", value = "Project Id", required = true) @NotNull @PathVariable Long projectId,
                              @NotNull @PathVariable Long staffingRequestId,
                              @NotNull @PathVariable String email) {
        staffingOperations.allocate(projectId, staffingRequestId, email + ".com", ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));
    }

    @RequestMapping(value = "/{projectId}/staffing-request/{staffingRequestId}/de-allocate/{email}/{lastworkingDate}",
            method = RequestMethod.PUT
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

    public void deAllocateStaff(@ApiParam(hidden = true) HttpServletRequest request,
                                @ApiParam(name = "projectId", value = "Project ID", required = true) @NotNull @PathVariable Long projectId,
                                @ApiParam(name = "staffingRequestId", value = "staffing Request Id", required = true) @NotNull @PathVariable Long staffingRequestId,
                                @ApiParam(name = "email", value = "Employee Email", required = true) @NotNull @PathVariable String email, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam(name = "lastworkingDate", value = "Employee Last Working Date it should be less than or equal to current date", required = true) Date lastworkingDate) {
        staffingOperations.deAllocate(projectId, staffingRequestId, email, ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()), lastworkingDate);
    }

    @RequestMapping(value = "/{projectId}/staffing-request/{staffingRequestId}/nominate/{email}",
            method = RequestMethod.PUT
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

    public void nominateStaff(@ApiParam(hidden = true) HttpServletRequest request,
                              @ApiParam(name = "projectId", value = "Project ID", required = true) @NotNull @PathVariable Long projectId,
                              @ApiParam(name = "staffingRequestId", value = "staffing Request Id", required = true) @NotNull @PathVariable Long staffingRequestId,
                              @ApiParam(name = "email", value = "Employee Email", required = true) @NotNull @PathVariable String email) {

        staffingOperations.nominate(projectId, staffingRequestId, email + ".com", ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));
    }

    @RequestMapping(value = "/{projectId}/staffing-request/{staffingRequestId}/reject/{email}",
            method = RequestMethod.PUT
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

    public void rejectStaff(@ApiParam(hidden = true) HttpServletRequest request,
                            @ApiParam(name = "projectId", value = "Project ID", required = true) @NotNull @PathVariable Long projectId,
                            @ApiParam(name = "staffingRequestId", value = "staffing Request Id", required = true) @NotNull @PathVariable Long staffingRequestId,
                            @ApiParam(name = "email", value = "Employee Email", required = true) @NotNull @PathVariable String email) {

        staffingOperations.rejectNomination(projectId, staffingRequestId, email + ".com", ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));
    }

    @RequestMapping(value = "/upcomingDeallocation",
            method = RequestMethod.POST,
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

    public Map<String, Integer> upcomingDeallocation(@ApiParam(name = "upcomingDeallocation", value = "upcomingDeallocation is a Map which require 3 key value pairs eg{days:12,competency:'JVM',regions:'US-WEST'}") @RequestBody LinkedHashMap<String, Object> upcomingDeallocation) {
        return staffingOperations.countUpcomingDeallocations((Integer) upcomingDeallocation.get("days"), (String) upcomingDeallocation.get("competency"), (ArrayList<String>) upcomingDeallocation.get("regions"));
    }

    @RequestMapping(value = "/upcompingDeallocationsList",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = UpcomingDeallocationDTO.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public Set<UpcomingDeallocationDTO> getUpComingDeallocations(@ApiParam(name = "upcomingDeallocation", value = "upcomingDeallocation is a Map which require 3 key value pairs eg{days:12,competency:'JVM',regions:'US-WEST'}") @RequestBody LinkedHashMap<String, Object> upcomingDeallocation) {
        String competency = null;
        if (upcomingDeallocation.get("competency") != null) {
            competency = (String) upcomingDeallocation.get("competency");
        }
        return staffingOperations.getUpcomingDeallocations((Integer) upcomingDeallocation.get("days"), (ArrayList<String>) upcomingDeallocation.get("regions"), competency);
    }


    @RequestMapping(value = "/allocationReport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Object.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})

    public List allocationReport(@ApiParam(name = "startDate", value = "Start Date", example = "15-05-2017") @RequestParam String startDate, @ApiParam(name = " endDate", value = "End date it should be greater than start date", example = "16-05-2017") @RequestParam String endDate) {
        return reportingService.allocationReportSummary(LocalDate.parse(startDate), LocalDate.parse(endDate));

    }


    @RequestMapping(value = "/deallocatedEmployees/{projectId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Object.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    List<StaffRequest> findDeallocatedEmployees(@NotNull @PathVariable Long projectId) {
        return staffingOperations.getDeallocateSummary(projectId);
    }

    @RequestMapping(value = "/reallocate", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Object.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    void reAllocate(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam @NotNull @RequestBody StaffRequest staffRequest) {
        staffingOperations.reAllocate(staffRequest, ((String) ((PreAuthenticatedAuthenticationToken) AuthenticatorResourceService.checkCache(request.getHeader("X-ACCESS-TOKEN")).getObjectValue()).getCredentials()));

    }
    @RequestMapping(value = "/upcomingDeAllocationsNotification", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void upcomingDeAllocationsNotification(@RequestParam Integer days){
        schedulerService.sendDeallocationEmailsNoSchedule(days);
    }
}