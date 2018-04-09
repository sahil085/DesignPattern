package com.ttn.bluebell.domain.project.billing;

import com.ttn.bluebell.domain.BaseModel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ashutoshmeher on 19/9/16.
 */

@Embeddable
public class EBillingMilestone{

    @Column( name = "NAME")
    @NotNull
    private String name;

    @Column(name = "AMOUNT")
    @NotNull
    private BigDecimal amount;

    @Column(name = "TENTATIVE_DATE")
    @NotNull
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
