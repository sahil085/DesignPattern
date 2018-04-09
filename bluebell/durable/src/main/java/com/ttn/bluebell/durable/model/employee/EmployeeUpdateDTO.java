package com.ttn.bluebell.durable.model.employee;

import java.text.ParseException;
import java.util.Date;

public class EmployeeUpdateDTO {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String username;
    private String dateOfJoining;
    private String employeeStatus;
    private String profilePicUrl;
    private String designation;
    private ReportingManagerDTO reportingManager;
    private ReportingManagerDTO performanceReviewer;
    private BusinessUnitDTO businessUnit;
    private FunctionDTO function;
    private RegionDTO region;
    private Competency competency;
    private LegalEntityDTO legalEntity;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfJoining() throws ParseException {
        Date date = null;
        if (this.dateOfJoining != null) {
            date = new Date(Long.parseLong(this.dateOfJoining));
        }
        return date;
    }

    public ReportingManagerDTO getPerformanceReviewer() {
        return performanceReviewer;
    }

    public void setPerformanceReviewer(ReportingManagerDTO performanceReviewer) {
        this.performanceReviewer = performanceReviewer;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public ReportingManagerDTO getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(ReportingManagerDTO reportingManager) {
        this.reportingManager = reportingManager;
    }

    public BusinessUnitDTO getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnitDTO businessUnit) {
        this.businessUnit = businessUnit;
    }

    public FunctionDTO getFunction() {
        return function;
    }

    public void setFunction(FunctionDTO function) {
        this.function = function;
    }

    public RegionDTO getRegion() {
        return region;
    }

    public void setRegion(RegionDTO region) {
        this.region = region;
    }

    public LegalEntityDTO getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntityDTO legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getName() {
        return this.firstName + " " + (this.middleName != null ? (this.middleName + " ") : "") + this.lastName;
    }

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }

   public class Competency {
        private String id;
        private String name;
        private String sfCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSfCode() {
            return sfCode;
        }

        public void setSfCode(String sfCode) {
            this.sfCode = sfCode;
        }
    }
}