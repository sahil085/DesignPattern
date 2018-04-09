package com.ttn.bluebell.domain.project.billing;

import com.ttn.bluebell.durable.model.project.billing.InvoiceInfo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ttnd on 29/9/16.
 */
@Embeddable
public class EInvoiceInfo {

    @Column(name = "INVOICE_START_DATE")
    @NotNull
    private Date invoiceStartDate;

    @Column(name = "INVOICE_CYCLE")
    @Enumerated(EnumType.STRING)
    @NotNull()
    private InvoiceInfo.InvoiceCycle invoiceCycle;

    public Date getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(Date invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public InvoiceInfo.InvoiceCycle getInvoiceCycle() {
        return invoiceCycle;
    }

    public void setInvoiceCycle(InvoiceInfo.InvoiceCycle invoiceCycle) {
        this.invoiceCycle = invoiceCycle;
    }
}
