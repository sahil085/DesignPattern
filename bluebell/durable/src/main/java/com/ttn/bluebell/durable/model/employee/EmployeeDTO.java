package com.ttn.bluebell.durable.model.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.HashMap;

/**
 * Created by ttn on 18/10/16.
 */

public class EmployeeDTO {

    private String username;
    private String employeeId;
    private String name;
    private String gender;
    private boolean trackable = false;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date DOJ;
    private String practice;
    private String serviceLine;
    private String legalEntity;
    private String function;
//    private HashMap<String,String> supervisor;
//    private HashMap<String,String> mentovisor;
    private String profilePicUrl;
    private String designation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isTrackable() {
        return trackable;
    }

    public void setTrackable(boolean trackable) {
        this.trackable = trackable;
    }

    public Date getDOJ() {
        return DOJ;
    }

    public void setDOJ(Date DOJ) {
        this.DOJ = DOJ;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(String serviceLine) {
        this.serviceLine = serviceLine;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

//    public HashMap<String, String> getSupervisor() {
//        return supervisor;
//    }
//
//    public void setSupervisor(HashMap<String, String> supervisor) {
//        this.supervisor = supervisor;
//    }
//
//    public HashMap<String, String> getMentovisor() {
//        return mentovisor;
//    }
//
//    public void setMentovisor(HashMap<String, String> mentovisor) {
//        this.mentovisor = mentovisor;
//    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public static Employee fromDTO(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setCode(employeeDTO.getEmployeeId());
        employee.setName(employeeDTO.getName());
        employee.setTitle(employeeDTO.getDesignation());
        employee.setImageUrl(employeeDTO.getProfilePicUrl());
        employee.setJoiningDate(employeeDTO.getDOJ());
        return employee;
    }
}
