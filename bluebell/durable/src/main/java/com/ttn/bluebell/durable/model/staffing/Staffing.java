package com.ttn.bluebell.durable.model.staffing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttn.bluebell.durable.model.employee.MentovisorDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by praveshsaini on 29/9/16.
 */
public class Staffing {

    public enum State{
        DeAllocated,
        Allocated,
        Nominated,
        NominationRejected
    }

    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String employeeName;
    private StaffRequest staffRequest;
    private State state;
    private String employeeCode;
    private MentovisorDTO mentovisor;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StaffRequest getStaffRequest() {
        return staffRequest;
    }

    public void setStaffRequest(StaffRequest staffRequest) {
        this.staffRequest = staffRequest;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public MentovisorDTO getMentovisor() {
        return mentovisor;
    }

    public void setMentovisor(MentovisorDTO mentovisor) {
        this.mentovisor = mentovisor;
    }
}
