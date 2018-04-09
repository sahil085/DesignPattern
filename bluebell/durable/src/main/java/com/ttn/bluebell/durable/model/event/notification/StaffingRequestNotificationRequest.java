package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.staffing.StaffRequest;

import java.util.List;

public class StaffingRequestNotificationRequest extends NotificationRequest {

    public StaffingRequestNotificationRequest(StaffRequest staffingRequest, String projectName, List<Employee> managers) {
        super(staffingRequest, "Open Need for : " + projectName, "new-staffing-request.vm", projectName, managers);
    }
}
