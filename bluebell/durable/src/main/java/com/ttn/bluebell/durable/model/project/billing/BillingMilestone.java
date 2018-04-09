package com.ttn.bluebell.durable.model.project.billing;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by praveshsaini on 22/9/16.
 */
public class BillingMilestone {

    @NotNull(message = "{name.required}")
    private String name;

    @NotNull(message = "{amount.required}")
    private BigDecimal amount;

    @NotNull(message = "{tentativeDate.required}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tentativeDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTentativeDate() {
        return tentativeDate;
    }

    public void setTentativeDate(Date tentativeDate) {
        this.tentativeDate = tentativeDate;
    }
}
