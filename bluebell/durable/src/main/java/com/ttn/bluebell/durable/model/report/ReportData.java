package com.ttn.bluebell.durable.model.report;

import java.time.LocalDate;

public class ReportData {
    private int allocationPercentage;
    private LocalDate date;
    private String billableType;

    public int getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(int allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBillableType(String billableType) {
        this.billableType = billableType;
    }

    public String getBillableType() {
        return billableType;
    }
}