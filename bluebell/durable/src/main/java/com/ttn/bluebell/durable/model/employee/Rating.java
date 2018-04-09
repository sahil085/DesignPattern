package com.ttn.bluebell.durable.model.employee;

import java.io.Serializable;

/**
 * Created by ttn on 2/11/16.
 */

public class Rating implements Serializable{
    private String cycle;
    private String value;
    private String feedback;

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
