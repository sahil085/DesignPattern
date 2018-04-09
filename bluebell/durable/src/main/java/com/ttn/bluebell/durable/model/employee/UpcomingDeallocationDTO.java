package com.ttn.bluebell.durable.model.employee;

import java.util.Date;

/**
 * Created by ttnd on 21/12/16.
 */
public class UpcomingDeallocationDTO  implements Comparable{

    private String employeeName;
    private String code;
    private String title;
    private String billableType;
    private String projectName;
    private String region;
    private String projectId;
    private Date date;
    private String competency;
    private Long regionId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }


    @Override
    public int compareTo(Object o) {
        UpcomingDeallocationDTO obj = (UpcomingDeallocationDTO) o;
        int projCompare = this.getProjectName().compareTo(obj.getProjectName());
        if(projCompare == 0){
            return this.getEmployeeName().compareTo(obj.getEmployeeName());
        }
        else{
            return projCompare;
        }
    }


    public String getBillableType() {
        return billableType;
    }

    public void setBillableType(String billableType) {
        this.billableType = billableType;
    }
}
