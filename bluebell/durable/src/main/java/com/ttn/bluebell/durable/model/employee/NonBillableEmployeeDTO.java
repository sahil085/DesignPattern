package com.ttn.bluebell.durable.model.employee;

import java.util.List;

/**
 * Created by amit on 5/6/17.
 */
public class NonBillableEmployeeDTO {
    private String employeeName;
    private String employeeCode;
    private String email;
    private Competency competency;
    private String title;
    private String region;
    private List<String> projectName;

    public NonBillableEmployeeDTO(Employee employee, List<String> projectName) {
        this.setEmail(employee.getEmailAddress());
        this.setCompetency(employee.getCompetency());
        this.setEmployeeCode(employee.getCode());
        this.setEmployeeName(employee.getName());
        this.setTitle(employee.getTitle());
        this.setRegion(employee.getDepartmentOfEmployee());
        this.setProjectName(projectName);

    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getProjectName() {
        return projectName;
    }

    public void setProjectName(List<String> projectName) {
        this.projectName = projectName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
