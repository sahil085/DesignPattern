package com.ttn.bluebell.domain.project.billing;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by ttnd on 23/9/16.
 */
@Embeddable
public class EPrice {

    @Column(name = "AMOUNT")
    @NotNull
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    @NotNull
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
