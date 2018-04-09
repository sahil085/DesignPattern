package com.ttn.bluebell.domain.staffing;

import com.ttn.bluebell.domain.BaseModel;
import com.ttn.bluebell.domain.project.EProject;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.enums.BillableType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ashutoshmeher on 19/9/16.
 */
@Entity
@Table(name = "PROJECT_STAFFING_REQUEST")
public class EProjectStaffingRequest extends BaseModel implements Cloneable{

    @Id
    @Column(name = "ID")
    @GenericGenerator(name="idGenerator", strategy="increment")
    @GeneratedValue(generator="idGenerator")
    private Long id;

    @Column(name = "TITLE")
    @NotNull
    private String title;

    @Column(name = "COMPETENCY")
    @NotNull
    private String competency;

    @Column(name = "BILLABLE")
    private Boolean billable;

    @Column(name = "BILL_TYPE")
    @Enumerated(EnumType.STRING)
    private BillableType billableType;

    @Column(name = "START_DATE")
    @NotNull
    private Date startDate;

    @Column(name = "END_DATE")
    @NotNull
    private Date endDate;

    @Column(name = "ADDITIONAL_DETAILS")
    private String details;

    @Column(name = "ALLOCATION_PERCENTAGE")
    @NotNull
    private Integer allocationPercentage;

    @Column(name="STATE")
    @NotNull
    @Enumerated(EnumType.STRING)
    private StaffRequest.State state = StaffRequest.State.Open;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private EProject project;

    @NotNull
    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "IS_REALLOCATED",columnDefinition = "boolean default false")
    protected Boolean isReallocated;

    public Boolean getReallocated() {
        return isReallocated;
    }

    public void setReallocated(Boolean reallocated) {
        isReallocated = reallocated;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public EProject getProject() {
        return project;
    }

    public void setProject(EProject project) {
        this.project = project;
    }

    public Integer getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(Integer allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public StaffRequest.State getState() {
        return state;
    }

    public void setState(StaffRequest.State state) {
        this.state = state;
    }
    public EProjectStaffingRequest(String title,String competency,Date startDate,Date endDate,String details,Integer allocationPercentage,StaffRequest.State state,EProject project,Boolean active,BillableType billableType){
           this.title=title;
           this.competency=competency;
           this.billableType = billableType;
           this.startDate=startDate;
           this.endDate=endDate;
           this.details=details;
           this.allocationPercentage=allocationPercentage;
           this.state=state;
           this.project= project;
           this.active=active;
            this.isReallocated = false;
    }


    public BillableType getBillableType() {
        return billableType;
    }

    public void setBillableType(BillableType billableType) {
        this.billableType = billableType;
    }


    public EProjectStaffingRequest(){}
    public Object clone() throws CloneNotSupportedException {
        EProjectStaffingRequest  cloneEProjectStaffingRequest =(EProjectStaffingRequest) super.clone();
        return new EProjectStaffingRequest(cloneEProjectStaffingRequest.title,cloneEProjectStaffingRequest.competency,startDate,cloneEProjectStaffingRequest.endDate,cloneEProjectStaffingRequest.details,cloneEProjectStaffingRequest.allocationPercentage,cloneEProjectStaffingRequest.state,cloneEProjectStaffingRequest.project,cloneEProjectStaffingRequest.active,cloneEProjectStaffingRequest.billableType);
    }

    @Override
    public String toString() {
        return "EProjectStaffingRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", competency='" + competency + '\'' +
                ", billable=" + billableType.toString() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", details='" + details + '\'' +
                ", allocationPercentage=" + allocationPercentage +
                ", state=" + state +
                ", project=" + project +
                ", active=" + active +
                '}';
    }
}
