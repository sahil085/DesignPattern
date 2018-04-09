package com.ttn.bluebell.durable.model.employee;

import java.util.List;

/**
 * Created by ttn on 19/10/16.
 */

public class EmployeeListDTO {
    private List<EmployeeDetailsDTO> user;

    public List<EmployeeDetailsDTO> getUser() {
        return user;
    }

    public void setUser(List<EmployeeDetailsDTO> user) {
        this.user = user;
    }
}

