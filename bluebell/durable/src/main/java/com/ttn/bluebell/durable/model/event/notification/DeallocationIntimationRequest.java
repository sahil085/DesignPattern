package com.ttn.bluebell.durable.model.event.notification;

import java.util.List;

/**
 * Created by ttnd on 20/10/16.
 */
public class DeallocationIntimationRequest<T> extends NotificationRequestWithPayload{

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    Integer days;


    public DeallocationIntimationRequest(String receiverName, String receiverEmail,String subject, List<String> ccEmails, T payload, Integer days) {
        super(receiverName, receiverEmail,subject , "deallocation-intimation.vm",ccEmails,payload);
        this.days = days;
    }
}