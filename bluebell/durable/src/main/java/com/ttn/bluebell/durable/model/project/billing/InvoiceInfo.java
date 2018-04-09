package com.ttn.bluebell.durable.model.project.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ttnd on 29/9/16.
 */
public class InvoiceInfo {

    public enum InvoiceCycle {
        Weekly,FortNight,Monthly
    }
    @ApiModelProperty(notes = "invoice start date")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceStartDate;
    @ApiModelProperty(notes = "invoice Cycle  Weekly/FortNight/Monthly",required = true)
    @NotNull(message = "{tnm.project.invoice.cycle.required}")
    private InvoiceCycle invoiceCycle;

    public Date getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(Date invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public InvoiceCycle getInvoiceCycle() {
        return invoiceCycle;
    }

    public void setInvoiceCycle(InvoiceCycle invoiceCycle) {
        this.invoiceCycle = invoiceCycle;
    }
}
