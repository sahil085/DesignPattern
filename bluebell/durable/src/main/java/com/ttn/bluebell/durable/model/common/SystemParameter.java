package com.ttn.bluebell.durable.model.common;

/**
 * Created by ttnd on 25/10/16.
 */

public class SystemParameter {

    public enum ParameterType{
        DEALLOCATION_INTIMATION_DAYS,
    }
    private String name;
    private String value;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
