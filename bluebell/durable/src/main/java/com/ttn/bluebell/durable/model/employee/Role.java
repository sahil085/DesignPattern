package com.ttn.bluebell.durable.model.employee;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ttn on 19/10/16.
 */
public enum Role {

    REGION_HEAD("REGION HEAD"),
    STAFFING_MANAGER("STAFFING MANAGER"),
    SYSTEM_ADMIN("SYSTEM ADMIN"),
    SALES_LEAD("SALES LEAD"),
    HRBP("HRBP");
    String name;
    Role(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Role getRole(String name){
        Map<String,Role> nameRolemap = Arrays.stream(Role.values()).collect(Collectors.toMap(val -> val.getName(), role -> role));

        if(nameRolemap.containsKey(name))
            return nameRolemap.get(name);
        else
            return null;
    }
}
