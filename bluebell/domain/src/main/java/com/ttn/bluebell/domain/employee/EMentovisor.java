package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE_MENTOVISOR")
public class EMentovisor extends BaseModel {
    @Id
    @Column(name = "EMP_CODE")
    private Long id;
    @Column(name = "EMPLOYEE_NAME", nullable = false)
    private String employeeName;
    @Column(name = "EMPLOYEE_EMAIL", nullable = false)
    private String employeeEmail;
    @Column(name = "MENTOVISOR_EMAIL", nullable = false)
    private String mentovisorEmail;

    @Column(name = "PERFORMANCE_REVIEWER_EMAIL")
    private String performanceReviewerEmail;

    public EMentovisor() {
    }

    public EMentovisor(Long id, String employeeName, String employeeEmail, String mentovisorEmail, String performanceReviewerEmail) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.mentovisorEmail = mentovisorEmail;
        this.performanceReviewerEmail = performanceReviewerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMentovisorEmail() {
        return mentovisorEmail;
    }

    public void setMentovisorEmail(String mentovisorEmail) {
        this.mentovisorEmail = mentovisorEmail;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getPerformanceReviewerEmail() {
        return performanceReviewerEmail;
    }

    public void setPerformanceReviewerEmail(String performanceReviewerEmail) {
        this.performanceReviewerEmail = performanceReviewerEmail;
    }
}
