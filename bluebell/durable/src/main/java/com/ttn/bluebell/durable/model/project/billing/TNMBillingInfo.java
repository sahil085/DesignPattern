package com.ttn.bluebell.durable.model.project.billing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class TNMBillingInfo extends BillingInfo {
    @ApiModelProperty(notes = "Invoice Info",required = true)
    @NotNull(message = "{tnm.project.invoiceInfo.required}")
    @Valid
    private InvoiceInfo invoiceInfo;
    @ApiModelProperty(notes = "List of Bill rates")
    @Valid
    private List<BillingRate> billingRates;

    public InvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(InvoiceInfo invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public List<BillingRate> getBillingRates() {
        return billingRates;
    }

    public void setBillingRates(List<BillingRate> billingRates) {
        this.billingRates = billingRates;
    }
}
