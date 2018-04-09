package com.ttn.bluebell.durable.model.project.billing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class Price {
    @ApiModelProperty(notes = "Amount",required = true)
    @NotNull(message = "{amount.required}")
    private BigDecimal amount;
    @ApiModelProperty(notes = "Currency Used",required = true)
    @NotNull(message = "{currency.required}")
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
