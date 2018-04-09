package com.ttn.bluebell.durable.model.employee;

public class CoreEntityDTO {
    String name;
    String sfCode;
    String coreEntityType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getCoreEntityType() {
        return coreEntityType;
    }

    public void setCoreEntityType(String coreEntityType) {
        this.coreEntityType = coreEntityType;
    }
}
