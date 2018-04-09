package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;

import java.util.List;

/**
 * Created by ttn on 29/9/16.
 */
public class RejectionNotificationRequest extends NotificationRequest{

    public RejectionNotificationRequest(String receiverName,String receiverEmail, String projectName, List<Employee> managers) {
        super(receiverName,receiverEmail, "Non consideration for : "+projectName, "rejection-template.vm", projectName, managers);
    }
}
