package com.ttn.bluebell.rest.services.project;

import com.ttn.bluebell.core.api.ProjectBasicOperations;
import com.ttn.bluebell.core.api.ProjectOperations;
import com.ttn.bluebell.core.exception.UnSupportedTypeException;
import com.ttn.bluebell.durable.model.common.ResponseBean;
import com.ttn.bluebell.durable.model.project.FixedPriceProject;
import com.ttn.bluebell.durable.model.project.NonBillableProject;
import com.ttn.bluebell.durable.model.project.Project;
import com.ttn.bluebell.durable.model.project.TimeAndMoneyProject;
import com.ttn.bluebell.durable.model.project.billing.BillingInfo;
import com.ttn.bluebell.durable.model.validation.Create;
import com.ttn.bluebell.durable.model.validation.ProjectCreate;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.*;

/**
 * Created by praveshsaini on 16/9/16.
 */
@RestController
@RequestMapping("/project")
public class ProjectResource {

    @Inject
    @Named("fixedPriceProjectTemplate")
    private ProjectOperations fixedProjectOperations;

    @Inject
    @Named("nonBillableProjectTemplate")
    private ProjectOperations nonBillableProjectOperations;

    @Inject
    @Named("timeAndMoneyProjectTemplate")
    private ProjectOperations timeAndMoneyProjectOperations;

    @Inject
    @Named("projectBasicTemplate")
    private ProjectBasicOperations basicOperations;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseBean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseBean createProject(@ApiParam @Validated ({ProjectCreate.class}) @RequestBody Project project) {
        return projectOperation(project.getClass()).create(project);
    }

    @RequestMapping(value = "/{projectId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  ResponseBean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseBean updateProject(@ApiParam(name ="projectId",required = true,value = "Project Id") @PathVariable Long projectId, @ApiParam @Validated ({Create.class, Default.class}) @RequestBody Project project) {
        return projectOperation(project.getClass()).update(projectId, project);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Project.class,responseContainer ="Set"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    List<Project> findAllProject() {

        Set<FixedPriceProject> fixedPriceProjects = fixedProjectOperations.findAll();
        Set<NonBillableProject> nonBillableProjects = nonBillableProjectOperations.findAll();
        Set<TimeAndMoneyProject> timeAndMoneyProjects = timeAndMoneyProjectOperations.findAll();

        Set<Project> projectSet=new HashSet<Project>();
        projectSet.addAll(fixedPriceProjects);
        projectSet.addAll(nonBillableProjects);
        projectSet.addAll(timeAndMoneyProjects);

        List<Project> projectList = new ArrayList<>(projectSet);
        Collections.sort(projectList,Project.OpenNeedComparator);
        return projectList;
    }

    @RequestMapping(value = "/{projectId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
             @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  Project.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
     public Project findProject(@ApiParam(name ="projectId",value = "Project ID",required = true) @NotNull @PathVariable  Long projectId) {

        return basicOperations.findById(projectId);
    }

    @RequestMapping(value = "/checkName/{projectName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
           @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Project.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Project findProjectByName(@ApiParam(name = "projectName")@PathVariable String projectName){
        return basicOperations.findProjectByName(projectName);
    }

    @RequestMapping(value = "/{projectId}",
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
    public void deleteProject(@ApiParam(value = "Project Id",required = true)@NotNull @PathVariable  Long projectId) {

         basicOperations.delete(projectId);
    }

    @RequestMapping(value = "/billable/{projectId}/billing-info",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BillingInfo.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})


    public BillingInfo updateBillingInfo(@ApiParam(name = "projectId" ,value = "Project ID",required = true)@NotNull @PathVariable  Long projectId, @ApiParam @Valid @RequestBody BillingInfo billingInfo) {
        Project project = findProject(projectId);
        billingInfo = projectOperation(project.getClass()).updateBillingInfo(projectId,billingInfo);
        return billingInfo;
    }

    @RequestMapping(value = "/billable/{projectId}/billing-info",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", paramType = "header", required = true, defaultValue = "ACCESS-TOKEN", value = "Application Types", dataType = "string")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response =  BillingInfo.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public BillingInfo getBillingInfo(@ApiParam(name = "projectId",required = true) @NotNull @PathVariable  Long projectId) {

        Project project = findProject(projectId);
        BillingInfo billingInfo = projectOperation(project.getClass()).getBillingInfo(projectId);
        return billingInfo;
    }

    private ProjectOperations projectOperation (Class projectType){

        if (projectType == FixedPriceProject.class){
            return fixedProjectOperations;
        }
        if (projectType == TimeAndMoneyProject.class){
            return timeAndMoneyProjectOperations;
        }
        if (projectType == NonBillableProject.class){
            return nonBillableProjectOperations;
        }
        throw new UnSupportedTypeException("exception.unsupported.project");
    }
}