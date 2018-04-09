package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;

import java.util.List;

/**
 * Created by deepak on 3/1/18.
 */
public class NotificationRequestWithPayload<T> {

    private String receiverName;
    public String receiverEmail;
private String subject;
    private String templateName;
    private List<String> ccEmails;
    private T payload;

    public NotificationRequestWithPayload(String receiverName, String receiverEmail, String subject, String templateName,List<String> ccEmails,T payload) {
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.templateName = templateName;
        this.payload = payload;
        this.ccEmails = ccEmails;
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


    @Override
    public String toString() {
        return "NotificationRequestWithPayload{" +
                "receiverName='" + receiverName + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", templateName='" + templateName + '\'' +
                '}';
    }

    public List<String> getCcEmails() {
        return ccEmails;
    }

    public void setCcEmails(List<String> ccEmails) {
        this.ccEmails = ccEmails;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
