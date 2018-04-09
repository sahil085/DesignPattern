package com.ttn.bluebell.durable.model.employee;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

public class ResignationDTO {
    @NotNull
    @ApiModelProperty(notes = "Employee Code",required = true)
    private String employeeCode;
    @ApiModelProperty(notes = "Reason or Last note before leaving")
    private String exitNote;
    @ApiModelProperty(notes = "Notice period of employee")
    private Long noticePeriod;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @ApiModelProperty(notes = "Date when resignation applied")
    private Date initiationDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @ApiModelProperty(notes = "Last working date of employee ")
    private Date lastWorkingDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resignDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastWorkDate;
    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getExitNote() {
        return exitNote;
    }

    public void setExitNote(String exitNote) {
        this.exitNote = exitNote;
    }

    public Long getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(Long noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public Date getInitiationDate() {
        return resignDate!=null?resignDate:initiationDate;
    }

    public void setInitiationDate(Date initiationDate) {
        this.initiationDate = initiationDate;
    }

    public Date getLastWorkingDate() {
        return lastWorkDate!=null?lastWorkDate:lastWorkingDate;
    }

    public void setResignDate(Date resignDate) {
        this.resignDate = resignDate;
    }

    public void setLastWorkDate(Date lastWorkDate) {
        this.lastWorkDate = lastWorkDate;
    }

    public void setLastWorkingDate(Date lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }

    @Override
    public String toString() {
        return "ResignationDTO{" +
                "employeeCode='" + employeeCode + '\'' +
                ", exitNote='" + exitNote + '\'' +
                ", noticePeriod=" + noticePeriod +
                ", initiationDate=" + initiationDate +
                ", lastWorkingDate=" + lastWorkingDate +
                '}';
    }
}
