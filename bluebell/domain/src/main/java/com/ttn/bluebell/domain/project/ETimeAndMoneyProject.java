package com.ttn.bluebell.domain.project;

import com.ttn.bluebell.domain.project.billing.ETNMBillingInfo;

import javax.persistence.*;

/**
 * Created by ashutoshmeher on 16/9/16.
 */
@Entity
@DiscriminatorValue("TIME_AND_MONEY")
public class ETimeAndMoneyProject extends EBillableProject{

    @Embedded
    private ETNMBillingInfo billingInfo;

    public ETNMBillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(ETNMBillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }
}
