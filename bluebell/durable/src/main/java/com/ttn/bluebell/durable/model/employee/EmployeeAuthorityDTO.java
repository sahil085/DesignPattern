package com.ttn.bluebell.durable.model.employee;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by ttnd on 22/10/16.
 */
public class EmployeeAuthorityDTO {

    @NotNull
    @ApiModelProperty(notes = "Employee Email",required = true)
    private String email;
    @ApiModelProperty(notes = "List of regions to be assign to a employee")
    private Set<String> regions;
    @ApiModelProperty(notes = "List of roles to be assign to a employee")
    private Set<String> roles;
    @ApiModelProperty(notes = "employee Data")
    private Employee employee;

    public EmployeeAuthorityDTO(String email,Employee employee,Set<String> regions,Set<String> roles){
        this.email = email;
        this.employee = employee;
        this.regions = regions;
        this.roles = roles;
    }

    public EmployeeAuthorityDTO(){

    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRegions() {
        return regions;
    }

    public void setRegions(Set<String> regions) {
        this.regions = regions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
