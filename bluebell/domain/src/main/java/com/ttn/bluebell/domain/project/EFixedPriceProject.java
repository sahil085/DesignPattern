package com.ttn.bluebell.domain.project;

import com.ttn.bluebell.domain.project.billing.EFixedPriceBillingInfo;

import javax.persistence.*;

/**
 * Created by ashutoshmeher on 16/9/16.
 */
@Entity
@DiscriminatorValue("FIXED_PRICE")
public class EFixedPriceProject extends EBillableProject{

    @Embedded
    private EFixedPriceBillingInfo billingInfo;

    public EFixedPriceBillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(EFixedPriceBillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }
}
