package com.ttn.bluebell.durable.enums;

/**
 * Created by deepak on 24/1/18.
 */
public enum BillableType {

    BILLABLE("Billable"),
    NON_BILLABLE("Non Billable"),
    SHADOW("Shadow");

    private final String billableType;


    BillableType(String billableType) {
        this.billableType = billableType;

    }

    public String getBillableType(){
        return billableType;
    }
}
