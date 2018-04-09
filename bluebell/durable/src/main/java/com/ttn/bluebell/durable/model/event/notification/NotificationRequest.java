package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;

import java.util.List;

/**
 * Created by ttn on 29/9/16.
 */

public abstract class NotificationRequest {
    private String receiverName;
    public String receiverEmail;
    private String subject;
    private String templateName;
    private String projectName;
    private StaffRequest staffRequest;
    List<Employee> managers;
    String startStaffingDate;
    String lastStaffingDate;

    public NotificationRequest(String receiverName, String receiverEmail, String subject, String templateName, String projectName, List<Employee> managers) {
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.templateName = templateName;
        this.projectName = projectName;
        this.managers = managers;
    }
    public NotificationRequest(String receiverName, String receiverEmail, String subject, String templateName, String projectName, List<Employee> managers,String startStaffingDate,String lastStaffingDate) {
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.templateName = templateName;
        this.projectName = projectName;
        this.managers = managers;
        this.startStaffingDate=startStaffingDate;
        this.lastStaffingDate=lastStaffingDate;
    }

    public NotificationRequest(StaffRequest staffRequest, String subject, String templateName, String projectName, List<Employee> managers) {
        this.receiverName = managers.get(0).getName();
        this.receiverEmail = managers.get(0).getEmailAddress();
        this.subject = subject;
        this.templateName = templateName;
        this.projectName = projectName;
        this.staffRequest = staffRequest;
        this.managers = managers;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getProjectName() {
        return projectName;
    }

    public List<Employee> getManagers() {
        return managers;
    }

    public StaffRequest getStaffRequest() {
        return staffRequest;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "receiverName='" + receiverName + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", templateName='" + templateName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", staffRequest=" + (staffRequest != null ? staffRequest.toString() : null) +
                ", managers=" + managers +
                '}';
    }

    public String getStartStaffingDate() {
        return startStaffingDate;
    }

    public void setStartStaffingDate(String startStaffingDate) {
        this.startStaffingDate = startStaffingDate;
    }

    public String getLastStaffingDate() {
        return lastStaffingDate;
    }

    public void setLastStaffingDate(String lastStaffingDate) {
        this.lastStaffingDate = lastStaffingDate;
    }
}
