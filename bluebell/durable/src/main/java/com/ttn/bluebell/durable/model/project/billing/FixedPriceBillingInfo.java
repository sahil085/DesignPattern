package com.ttn.bluebell.durable.model.project.billing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class    FixedPriceBillingInfo extends BillingInfo {
    @ApiModelProperty(notes = "Price of the project")
    @NotNull(message = "{fixed.price.project.value.required}")
    @Valid
    private Price projectValue;

    @Valid
    private List<BillingMilestone> billingMilestones;

    public Price getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(Price projectValue) {
        this.projectValue = projectValue;
    }

    public List<BillingMilestone> getBillingMilestones() {
        return billingMilestones;
    }

    public void setBillingMilestones(List<BillingMilestone> billingMilestones) {
        this.billingMilestones = billingMilestones;
    }
}
