package com.ttn.bluebell.durable.model.employee;

import com.ttn.bluebell.durable.model.staffing.StaffRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by praveshsaini on 29/9/16.
 */
public class Employee implements Serializable {
    private String code;
    private String name;
    private String title;
    private Competency competency;
    private Date joiningDate;
    private Integer noOfYearsExp;
    private List<ReviewerFeedback> reviewerFeedback;
    private AdditionalInfo additionalInfo;
    private String emailAddress;
    private String imageUrl;
    private String function;
    private String businessUnit;
    private String departmentOfEmployee;
    private String mentovisor;
    private Employee mentovisorData;
    private int leavesCount;

    public int getLeavesCount() {
        return leavesCount;
    }

    public void setLeavesCount(int leavesCount) {
        this.leavesCount = leavesCount;
    }

    private String performanceReviewerEmail;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Integer getNoOfYearsExp() {
        return noOfYearsExp;
    }

    public void setNoOfYearsExp(Integer noOfYearsExp) {
        this.noOfYearsExp = noOfYearsExp;
    }

    public List<ReviewerFeedback> getReviewerFeedback() {
        return reviewerFeedback;
    }

    public void setReviewerFeedback(List<ReviewerFeedback> reviewerFeedback) {
        this.reviewerFeedback = reviewerFeedback;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDepartmentOfEmployee() {
        return departmentOfEmployee;
    }

    public void setDepartmentOfEmployee(String departmentOfEmployee) {
        this.departmentOfEmployee = departmentOfEmployee;
    }

    public String getMentovisor() {
        return mentovisor;
    }

    public void setMentovisor(String mentovisor) {
        this.mentovisor = mentovisor;
    }

    public Employee getMentovisorData() {
        return mentovisorData;
    }

    public void setMentovisorData(Employee mentovisorData) {
        this.mentovisorData = mentovisorData;
    }

    public static class AdditionalInfo implements Serializable {
        private Set<String> previousProjects;
        private Map<String, StaffRequest> currentBillableProjects;
        private Map<String, StaffRequest> currentNonBillableProjects;
        private Map<String, StaffRequest> currentShadowProjects;
        private Map<String, StaffRequest> nominatedProjects;
        private Integer totalBillableAllocation;
        private Integer totalNonBillableAllocation;
        private Integer totalShadowAllocation;


        public Set<String> getPreviousProjects() {
            return previousProjects;
        }

        public void setPreviousProjects(Set<String> previousProjects) {
            this.previousProjects = previousProjects;
        }

        public Integer getTotalBillableAllocation() {
            return totalBillableAllocation;
        }

        public void setTotalBillableAllocation(Integer totalBillableAllocation) {
            this.totalBillableAllocation = totalBillableAllocation;
        }

        public Integer getTotalNonBillableAllocation() {
            return totalNonBillableAllocation;
        }

        public void setTotalNonBillableAllocation(Integer totalNonBillableAllocation) {
            this.totalNonBillableAllocation = totalNonBillableAllocation;
        }

        public Map<String, StaffRequest> getCurrentBillableProjects() {
            return currentBillableProjects;
        }

        public void setCurrentBillableProjects(Map<String, StaffRequest> currentBillableProjects) {
            this.currentBillableProjects = currentBillableProjects;
        }

        public Map<String, StaffRequest> getCurrentNonBillableProjects() {
            return currentNonBillableProjects;
        }

        public void setCurrentNonBillableProjects(Map<String, StaffRequest> currentNonBillableProjects) {
            this.currentNonBillableProjects = currentNonBillableProjects;
        }

        public Map<String, StaffRequest> getCurrentShadowProjects() {
            return currentShadowProjects;
        }

        public void setCurrentShadowProjects(Map<String, StaffRequest> currentShadowProjects) {
            this.currentShadowProjects = currentShadowProjects;
        }

        public Integer getTotalShadowAllocation() {
            return totalShadowAllocation;
        }

        public void setTotalShadowAllocation(Integer totalShadowAllocation) {
            this.totalShadowAllocation = totalShadowAllocation;
        }
    }

    public String getPerformanceReviewerEmail() {
        return performanceReviewerEmail;
    }

    public void setPerformanceReviewerEmail(String performanceReviewerEmail) {
        this.performanceReviewerEmail = performanceReviewerEmail;
    }
}
