package com.ttn.bluebell.durable.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttn.bluebell.durable.model.client.Client;
import com.ttn.bluebell.durable.model.common.ProjectRegion;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;
import com.ttn.bluebell.durable.model.validation.Create;
import com.ttn.bluebell.durable.model.validation.ProjectCreate;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by praveshsaini on 16/9/16.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_Type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedPriceProject.class, name = "fixedPrice"),
        @JsonSubTypes.Type(value = NonBillableProject.class, name = "nonBillable"),
        @JsonSubTypes.Type(value = TimeAndMoneyProject.class, name = "timeAndMoney")
})

@ProjectCreate(groups = Create.class)
public abstract class Project {

    public enum Type {
        Billable,
        NonBillable
    }

    protected Long projectId;

    @ApiModelProperty(notes = "Type of the project  Billable/NonBillable ", required = true)
    @NotNull(message = "{project.type.required}")
    protected Type projectType;
    @ApiModelProperty(notes = "Project Name", required = true)
    @JsonProperty
    @NotNull(message = "{project.name.required}")
    protected String projectName;
    @ApiModelProperty(notes = "Client Associate with the project", required = true)
    @NotNull(message = "{client.name.required}")
    protected Client client;
    @ApiModelProperty(notes = "Buisness Unit Of the project", required = true)
    @NotNull(message = "{business.unit.required}")
    protected String businessUnit;
    @ApiModelProperty(notes = "Region of the project", required = true)
    @NotNull(message = "{region.required}")
    protected String region;
    @ApiModelProperty(notes = "Region of the project", required = true)
    protected ProjectRegion regionvalue;
    @ApiModelProperty(notes = "Project start date")
    @NotNull(message = "{start.date.required}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date startDate;
    @ApiModelProperty(notes = "Project end Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date endDate;
    @ApiModelProperty(notes = "Staff Request List for the project")
    @Valid
    @JsonProperty("staffRequestList")
    protected List<StaffRequest> staffRequests;
    @ApiModelProperty(notes = "staff request active or not")
    protected Boolean active = true;
    @ApiModelProperty(notes = "Number of  open Staff Requests")
    private Integer openStaffRequests;

    public Integer getOpenStaffRequests() {
        return openStaffRequests;
    }

    public void setOpenStaffRequests(Integer openStaffRequests) {
        this.openStaffRequests = openStaffRequests;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public Type getProjectType() {
        return projectType;
    }

    public void setProjectType(Type projectType) {
        this.projectType = projectType;
    }

    public List<StaffRequest> getStaffRequests() {
        return staffRequests;
    }

    public void setStaffRequests(List<StaffRequest> staffRequests) {
        this.staffRequests = staffRequests;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProjectRegion getRegionvalue() {
        return regionvalue;
    }

    public void setRegionvalue(ProjectRegion regionvalue) {
        this.regionvalue = regionvalue;
    }

    public static Comparator<Project> OpenNeedComparator = new Comparator<Project>() {

        @Override
        public int compare(Project o1, Project o2) {
            int cmpVal = 0;
            if (o2.getOpenStaffRequests() == null)
                return -1;
            else
                cmpVal = o2.getOpenStaffRequests().compareTo(o1.getOpenStaffRequests());
            if (cmpVal == 0) {
                return o1.getProjectName().toLowerCase().compareTo(o2.getProjectName().toLowerCase());
            } else {
                return cmpVal;
            }
        }
    };

}

