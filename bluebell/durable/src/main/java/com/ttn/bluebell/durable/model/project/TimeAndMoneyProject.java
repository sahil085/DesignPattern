package com.ttn.bluebell.durable.model.project;

import com.ttn.bluebell.durable.model.project.billing.TNMBillingInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by ashutoshmeher on 16/9/16.
 */
public class TimeAndMoneyProject extends BillableProject {
    @NotNull
    @Valid
    private TNMBillingInfo billingInfo;

    public TNMBillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(TNMBillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }
}
