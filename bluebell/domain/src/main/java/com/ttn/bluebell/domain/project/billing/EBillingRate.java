package com.ttn.bluebell.domain.project.billing;

import com.ttn.bluebell.domain.BaseModel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

/**
 * Created by ashutoshmeher on 19/9/16.
 */

@Embeddable
public class EBillingRate{

    @Column(name="TITLE")
    @NotNull
    private String title;

    @Embedded
    @NotNull
    private EPrice rate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EPrice getRate() {
        return rate;
    }

    public void setRate(EPrice rate) {
        this.rate = rate;
    }


}
