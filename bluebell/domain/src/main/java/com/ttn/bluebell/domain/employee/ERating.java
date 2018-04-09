package com.ttn.bluebell.domain.employee;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Created by ttn on 2/11/16.
 */

@Embeddable
public class ERating {

    @NotNull
    @Column(name = "CYCLE")
    private String cycle;

    @NotNull
    @Column(name = "VALUE")
    private String value;

    @Column(name = "FEEDBACK")
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
