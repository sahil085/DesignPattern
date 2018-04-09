package com.ttn.bluebell.durable.model.staffing;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by ttnd on 1/1/17.
 */
public class AllocationDetails implements Comparable {

    private String projectName;
    private int allocationPercentage;
    private String competency;
    private String email;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String region;
    private String billableType;
    private String clientName;

    public AllocationDetails() {
    }

    public AllocationDetails(String projectName, int allocationPercentage, String competency, String email, String title, String region) {
        this.projectName = projectName;
        this.allocationPercentage = allocationPercentage;
        this.competency = competency;
        this.email = email;
        this.title = title;
        this.region = region;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(int allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    @Override
    public int compareTo(Object o) {
        AllocationDetails details = (AllocationDetails) o;
        int dateComp = this.getStartDate().compareTo(((AllocationDetails) o).getStartDate());
        if (dateComp == 0) {
            int isprojectSame = this.getProjectName().compareTo(((AllocationDetails) o).getProjectName());
            if (isprojectSame == 0 && (this.getEndDate() != null) && (((AllocationDetails) o).getEndDate() != null)) {
                return this.getEndDate().compareTo(((AllocationDetails) o).getEndDate());
            } else {
                return isprojectSame;
            }

        } else {
            return dateComp;
        }
    }

    public String getBillableType() {
        return billableType;
    }

    public void setBillableType(String billableType) {
        this.billableType = billableType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
