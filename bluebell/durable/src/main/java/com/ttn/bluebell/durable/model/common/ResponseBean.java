package com.ttn.bluebell.durable.model.common;

import java.util.List;

/**
 * Created by ashutoshmeher on 19/9/16.
 */
public class ResponseBean {

    private String status;

    private List<Message> messages;

    private ResponseContent content;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ResponseContent getContent() {
        return content;
    }

    public void setContent(ResponseContent content) {
        this.content = content;
    }
}
