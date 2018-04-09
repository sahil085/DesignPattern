package com.ttn.bluebell.domain.project;

import com.ttn.bluebell.durable.model.project.BillableProject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by praveshsaini on 16/9/16.
 */
@MappedSuperclass
public abstract class EBillableProject extends EProject {

    @Column(name = "BILLING_TYPE")
    @Enumerated(EnumType.STRING)
    @NotNull
    private BillableProject.BillingType billingType;

    public BillableProject.BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillableProject.BillingType billingType) {
        this.billingType = billingType;
    }
}
