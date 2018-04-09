package com.ttn.bluebell.domain.project;

import com.ttn.bluebell.domain.BaseModel;
import com.ttn.bluebell.domain.client.EClient;
import com.ttn.bluebell.domain.region.Region;
import com.ttn.bluebell.domain.staffing.EProjectStaffingRequest;
import com.ttn.bluebell.durable.model.project.Project;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by praveshsaini on 16/9/16.
 */
@Entity
@Table(name = "PROJECT_DETAILS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "D_TYPE",
        discriminatorType = DiscriminatorType.STRING
)
public abstract class EProject extends BaseModel {

    @Id
    @Column(name = "PROJECT_ID")
    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(generator = "idGenerator")
    protected Long projectId;

    @Column(name = "PROJECT_TYPE")
    @Enumerated(EnumType.STRING)
    @NotNull
    protected Project.Type projectType;

    @Column(name = "PROJECT_NAME")
    @NotNull
    protected String projectName;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    protected EClient client;

    @Column(name = "BUSINESS_UNIT")
    @NotNull
    protected String businessUnit;

    @Column(name = "REGION")
    @NotNull
    protected String region;

    @OneToOne
    @JoinColumn(name = "REGION_ID")
    protected Region regionvalue;

    @Column(name = "START_DATE")
    @NotNull
    protected Date startDate;

    @Column(name = "END_DATE")
    protected Date endDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROJECT_ID")
    protected List<EProjectStaffingRequest> staffRequests;

    @NotNull
    @Column(name = "ACTIVE")
    protected Boolean active;

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

    public EClient getClient() {
        return client;
    }

    public void setClient(EClient client) {
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
        this.startDate = new Date(startDate.getTime());
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Project.Type getProjectType() {
        return projectType;
    }

    public void setProjectType(Project.Type projectType) {
        this.projectType = projectType;
    }

    public List<EProjectStaffingRequest> getStaffRequests() {
        return staffRequests;
    }

    public void setStaffRequests(List<EProjectStaffingRequest> staffRequests) {
        this.staffRequests = staffRequests;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Region getRegionvalue() {
        return regionvalue;
    }

    public void setRegionvalue(Region regionvalue) {
        this.regionvalue = regionvalue;
    }
}
