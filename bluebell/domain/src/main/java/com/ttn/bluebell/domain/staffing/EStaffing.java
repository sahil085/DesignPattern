package com.ttn.bluebell.domain.staffing;

import com.ttn.bluebell.domain.BaseModel;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.durable.model.staffing.Staffing.State;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by ttnd on 3/10/16.
 */
@Entity
@Table(name = "STAFFING_DETAIL")
public class EStaffing extends BaseModel {

    @Id
    @Column(name = "ALLOCATION_ID")
    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(generator = "idGenerator")
    protected Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "PROJECT_ID")
    protected EProject project;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "STAFF_REQUEST_ID")
    protected EProjectStaffingRequest staffingRequest;

    @Column(name = "EMAIL")
    @NotNull
    protected String email;

    @Column(name = "EMP_NAME")
    @NotNull
    protected String employeeName;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    @NotNull
    protected State state;

    @Column(name = "DATE")
    @NotNull
    protected LocalDate date;

    public EStaffing() {
    }

    public EStaffing(EStaffing eStaffing,EProjectStaffingRequest newstaffingRequest) {
        this.project = eStaffing.project;
        this.staffingRequest = newstaffingRequest;
        this.email = eStaffing.email;
        this.employeeName = eStaffing.employeeName;
        this.state = eStaffing.state;
        this.date = eStaffing.date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EProject getProject() {
        return project;
    }

    public void setProject(EProject project) {
        this.project = project;
    }

    public EProjectStaffingRequest getStaffingRequest() {
        return staffingRequest;
    }

    public void setStaffingRequest(EProjectStaffingRequest staffingRequest) {
        this.staffingRequest = staffingRequest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
