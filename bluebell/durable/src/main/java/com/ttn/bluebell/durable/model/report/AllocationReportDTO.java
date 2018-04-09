package com.ttn.bluebell.durable.model.report;


import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.staffing.AllocationDetails;

import java.util.List;

public class AllocationReportDTO {
    private Employee employee;
    private String projectName;
    private String competency;
    private String region;
    private String clientName;
    private List<ReportData> reportData;

    public AllocationReportDTO() {
    }

    public AllocationReportDTO(Employee employee, AllocationDetails allocationDetail, List<ReportData> reportData) {
        this.employee = employee;
        this.projectName = allocationDetail.getProjectName();
        this.competency = allocationDetail.getCompetency();
        this.region = allocationDetail.getRegion();
        this.reportData = reportData;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<ReportData> getReportData() {
        return reportData;
    }

    public void setReportData(List<ReportData> ReportData) {
        this.reportData = ReportData;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
