package com.ttn.bluebell.durable.model.project;

import javax.validation.constraints.NotNull;

/**
 * Created by praveshsaini on 16/9/16.
 */
public abstract class BillableProject extends Project {

    public enum BillingType {
        FixedPrice,
        TimeAndMoney
    }

    @NotNull(message = "{billing.type.required}")
    private BillingType billingType;

    public BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }






}
