package com.ttn.bluebell.durable.model.event.notification;

import com.ttn.bluebell.durable.model.employee.Employee;

import java.util.List;

/**
 * Created by ttnd on 19/10/16.
 */
public class InvoiceNotificationRequest extends NotificationRequest {

    public InvoiceNotificationRequest(String receiverName, String receiverEmail, String projectName, List<Employee> managers) {
        super(receiverName, receiverEmail, "Invoice Reminder for "+projectName, "invoice-template.vm", projectName, managers);
    }
}
