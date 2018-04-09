package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;

import java.util.List;

/**
 * Created by deepak on 4/1/18.
 */
public class AllocationNewerNotificationRequest extends NotificationRequest {

    public AllocationNewerNotificationRequest(String receiverName, String receiverEmail, String projectName, List<Employee> managers, String startStaffingDate, String lastStaffingDate) {
        super(receiverName, receiverEmail, "Allocated to project : " + projectName, "allocation-newer-template.vm", projectName, managers, startStaffingDate, lastStaffingDate);
    }
}
