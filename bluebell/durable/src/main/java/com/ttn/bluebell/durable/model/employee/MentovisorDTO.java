package com.ttn.bluebell.durable.model.employee;

import java.util.List;

public class MentovisorDTO {
    private String code;
    private String name;
    private String emailAddress;
    private Competency competency;
    private String region;
    private String menteeRegion;
    private String menteename;
    private String menteeEmailAddress;
    private String menteeCode;
    private Competency menteeCompetency;
    private String performanceReviewerEmail;

    public MentovisorDTO() {
    }

    public MentovisorDTO(Employee employee, Employee mentee, String performanceReviewerEmail) {
        this.code = employee.getCode();
        this.name = employee.getName();
        this.emailAddress = employee.getEmailAddress();
        this.competency = employee.getCompetency();
        this.menteename = mentee.getName();
        this.menteeEmailAddress = mentee.getEmailAddress();
        this.menteeCompetency = mentee.getCompetency();
        this.performanceReviewerEmail = performanceReviewerEmail;
        this.menteeCode = mentee.getCode();
    }

    public MentovisorDTO(Employee employee, String region, String mentovisorEmail, String performanceReviewerEmail) {
        this.emailAddress = mentovisorEmail;
        this.menteename = employee.getName();
        this.menteeEmailAddress = employee.getEmailAddress();
        this.menteeCompetency = employee.getCompetency();
        this.performanceReviewerEmail = performanceReviewerEmail;
        this.menteeCode = employee.getCode();
        this.menteeRegion=region;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMenteeRegion() {
        return menteeRegion;
    }

    public void setMenteeRegion(String menteeRegion) {
        this.menteeRegion = menteeRegion;
    }

    public String getPerformanceReviewerEmail() {
        return performanceReviewerEmail;
    }

    public void setPerformanceReviewerEmail(String performanceReviewerEmail) {
        this.performanceReviewerEmail = performanceReviewerEmail;
    }

    public String getMenteename() {
        return menteename;
    }

    public void setMenteename(String menteename) {
        this.menteename = menteename;
    }

    public String getMenteeEmailAddress() {
        return menteeEmailAddress;
    }

    public void setMenteeEmailAddress(String menteeEmailAddress) {
        this.menteeEmailAddress = menteeEmailAddress;
    }

    public Competency getMenteeCompetency() {
        return menteeCompetency;
    }

    public void setMenteeCompetency(Competency menteeCompetency) {
        this.menteeCompetency = menteeCompetency;
    }

    public String getMenteeCode() {
        return menteeCode;
    }

    public void setMenteeCode(String menteeCode) {
        this.menteeCode = menteeCode;
    }
}
