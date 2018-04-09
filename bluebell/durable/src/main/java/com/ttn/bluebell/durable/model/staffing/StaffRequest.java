package com.ttn.bluebell.durable.model.staffing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttn.bluebell.durable.enums.BillableType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StaffRequest implements Serializable {

    public BillableType getBillableType() {
        return billableType;
    }

    public void setBillableType(BillableType billableType) {
        this.billableType = billableType;
    }

    public enum State {
        Closed,
        Open
    }

    @ApiModelProperty(notes = "Staff Id")
    private Long id;
    @ApiModelProperty(notes = "Staff title", required = true)
    @NotNull(message = "{staff.title.required}")
    private String title;
    @ApiModelProperty(notes = "Competency", required = true)
    @NotNull(message = "{competency.required}")
    private String competency;
    @ApiModelProperty(notes = "Staff request starting date", required = true)
    @NotNull(message = "{start.date.required}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @ApiModelProperty(notes = "Staff Request end date", required = true)
    @NotNull(message = "{end.date.required}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(notes = "Billable/non-Billable/shadow", required = true)
    @NotNull(message = "{staff.billable.required}")
    private BillableType billableType;

    @ApiModelProperty(notes = "details of staff request")
    private String details;
    @ApiModelProperty(notes = "staff allocation percentage", required = true)
    @NotNull(message = "{staff.allocationPercentage.required}")
    @JsonProperty("allocation")
    private Integer allocationPercentage;
    @ApiModelProperty(notes = "isAddNew triggers mail when true")
    private boolean isAddNew;
    @ApiModelProperty(notes = "staff State open/closed")
    private State state;
    private Boolean active = true;

    private List<Staffing> nominatedStaffings;

    private Staffing allocatedStaffing;

    @ApiModelProperty(notes = "Project Id")
    private Long projectId;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Staffing> getNominatedStaffings() {
        return nominatedStaffings;
    }

    public void setNominatedStaffings(List<Staffing> nominatedStaffings) {
        this.nominatedStaffings = nominatedStaffings;
    }

    public Staffing getAllocatedStaffing() {
        return allocatedStaffing;
    }

    public void setAllocatedStaffing(Staffing allocatedStaffing) {
        this.allocatedStaffing = allocatedStaffing;
    }

    public boolean getIsAddNew() {
        return isAddNew;
    }

    public void setAddNew(boolean addNew) {
        isAddNew = addNew;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "StaffRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", competency='" + competency + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", billableType=" + billableType.toString() +
                ", details='" + details + '\'' +
                ", allocationPercentage=" + allocationPercentage +
                ", state=" + state +
                ", active=" + active +
                ", nominatedStaffings=" + nominatedStaffings +
                ", allocatedStaffing=" + allocatedStaffing +
                '}';
    }
}
