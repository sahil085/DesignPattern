package com.ttn.bluebell.durable.model.common;

/**
 * Created by  on 19/9/16.
 */
public class Message {

    private MessageType messageType;

    private String message;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
