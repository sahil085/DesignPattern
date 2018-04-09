package com.ttn.bluebell.domain.project.billing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ttnd on 23/9/16.
 */

@Embeddable
public class ETNMBillingInfo extends EBillingInfo {

    @NotNull
    @Embedded
    private EInvoiceInfo invoiceInfo;

    @ElementCollection
    @CollectionTable( name = "BILLING_RATE", joinColumns = @JoinColumn(name = "project_id") )
    @OrderColumn(name = "billing_rate_id")
    private List<EBillingRate> billingRates;

    public EInvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(EInvoiceInfo invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public List<EBillingRate> getBillingRates() {
        return billingRates;
    }

    public void setBillingRates(List<EBillingRate> billingRates) {
        this.billingRates = billingRates;
    }
}
