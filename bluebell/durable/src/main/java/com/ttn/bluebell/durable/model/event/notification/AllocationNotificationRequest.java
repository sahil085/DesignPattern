package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;

import java.util.List;

/**
 * Created by ttn on 29/9/16.
 */
public class AllocationNotificationRequest extends NotificationRequest {

    public AllocationNotificationRequest(String receiverName, String receiverEmail, String projectName, List<Employee> managers, String startStaffingDate, String lastStaffingDate) {
        super(receiverName, receiverEmail, "Allocated to project : " + projectName, "allocation-template.vm", projectName, managers, startStaffingDate, lastStaffingDate);
    }
}
