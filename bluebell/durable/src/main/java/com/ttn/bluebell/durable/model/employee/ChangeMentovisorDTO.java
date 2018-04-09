package com.ttn.bluebell.durable.model.employee;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ChangeMentovisorDTO {
    @NotNull
    @ApiModelProperty(notes = "Employee Code", required = true)
    private Long empCode;
    @NotNull
    @ApiModelProperty(notes = "Employee Name", required = true)
    private String employeeName;
    @NotNull
    @ApiModelProperty(notes = "Employee mentovisor Email", required = true)
    private String mentovisorEmail;
    @NotNull
    @ApiModelProperty(notes = "New region assign to the Employee ", required = true)
    private String newRegion;
    @ApiModelProperty(notes = "project Id in which employee is being allocating/deallocating")
    private String projectId;
    @ApiModelProperty(notes = "Employee current staff Id for a project")
    private String currStaffId;
    @ApiModelProperty(notes = "Automatically sets this value by current user")
    private String modifiedByEmployeeCode;
    @ApiModelProperty(notes = "Atomatically set the value by cufrent date")
    private Date modifiedOn;
    @ApiModelProperty(notes = "Employee performanceReviewer Email", required = true)
    private String performanceReviewerEmail;

    public ChangeMentovisorDTO() {
    }

    public ChangeMentovisorDTO(Long empCode, String mentovisorEmail,String performanceReviewerEmail) {
        this.empCode = empCode;
        this.mentovisorEmail = mentovisorEmail;
        this.performanceReviewerEmail = performanceReviewerEmail;
    }

    public Long getEmpCode() {
        return empCode;
    }

    public void setEmpCode(Long empCode) {
        this.empCode = empCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMentovisorEmail() {
        return mentovisorEmail;
    }

    public void setMentovisorEmail(String mentovisorEmail) {
        this.mentovisorEmail = mentovisorEmail;
    }

    public String getNewRegion() {
        return newRegion;
    }

    public void setNewRegion(String newRegion) {
        this.newRegion = newRegion;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCurrStaffId() {
        return currStaffId;
    }

    public void setCurrStaffId(String currStaffId) {
        this.currStaffId = currStaffId;
    }

    public String getModifiedByEmployeeCode() {
        return modifiedByEmployeeCode;
    }

    public void setModifiedByEmployeeCode(String modifiedByEmployeeCode) {
        this.modifiedByEmployeeCode = modifiedByEmployeeCode;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getPerformanceReviewerEmail() {
        return performanceReviewerEmail;
    }

    public void setPerformanceReviewerEmail(String performanceReviewerEmail) {
        this.performanceReviewerEmail = performanceReviewerEmail;
    }
}
