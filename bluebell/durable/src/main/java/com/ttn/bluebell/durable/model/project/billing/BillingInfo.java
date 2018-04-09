package com.ttn.bluebell.durable.model.project.billing;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by praveshsaini on 23/9/16.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedPriceBillingInfo.class, name = "fixedPrice"),
        @JsonSubTypes.Type(value = TNMBillingInfo.class, name = "timeAndMoney")
})

public abstract class BillingInfo {
    @ApiModelProperty(notes = "Billing Info ID")
    private Long billingInfoId;

    public Long getBillingInfoId() {
        return billingInfoId;
    }

    public void setBillingInfoId(Long billingInfoId) {
        this.billingInfoId = billingInfoId;
    }
}
