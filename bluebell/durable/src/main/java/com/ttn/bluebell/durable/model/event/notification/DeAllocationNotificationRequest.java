package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;

import java.util.List;

/**
 * Created by ttn on 29/9/16.
 */
public class DeAllocationNotificationRequest extends NotificationRequest {

    public DeAllocationNotificationRequest(String receiverName, String receiverEmail, String projectName, List<Employee> managers, String startStaffingDate, String lastStaffingDate) {
        super(receiverName, receiverEmail, "Deallocated from project : " + projectName, "deallocation-template.vm", projectName, managers, startStaffingDate, lastStaffingDate);
    }

}
