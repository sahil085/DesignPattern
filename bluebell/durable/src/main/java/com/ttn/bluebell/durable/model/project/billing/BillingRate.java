package com.ttn.bluebell.durable.model.project.billing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by praveshsaini on 22/9/16.
 */
public class BillingRate {
    @ApiModelProperty(notes = "title",required = true)
    @NotNull
    private String title;
    @ApiModelProperty(notes = "Price",required = true)
    @NotNull
    @Valid
    private Price rate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Price getRate() {
        return rate;
    }

    public void setRate(Price rate) {
        this.rate = rate;
    }
}
