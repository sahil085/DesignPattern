package com.ttn.bluebell.durable.model.project;

import com.ttn.bluebell.durable.model.project.billing.FixedPriceBillingInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by ashutoshmeher on 16/9/16.
 */

public class FixedPriceProject extends BillableProject {

    @Valid
    @NotNull
    private FixedPriceBillingInfo billingInfo;

    public FixedPriceBillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(FixedPriceBillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }
}
