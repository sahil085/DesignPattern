package com.ttn.bluebell.durable.model.employee;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Created by ttnd on 22/10/16.
 */
public class EmployeeCompetencyTitleListDTO {

    @NotNull
    @ApiModelProperty(notes = "List of Competency")
    private Set<String> competencies;
    @ApiModelProperty(notes = "List of Title")
    private Set<String> titles;
    @ApiModelProperty(notes = "Regular expression filter for name or email or employee code")
    private String search;
    @ApiModelProperty(notes = "Include Resigned employee ")
    private Boolean includeResigned;
    @ApiModelProperty(notes = "Non-bliiable min value")
    private float nonBillableMin;
    @ApiModelProperty(notes = "Non=billable max value")
    private float nonBillableMax;
    @ApiModelProperty(notes = "Blillable min value")
    private float billiableMin;
    @ApiModelProperty(notes = "Billable Max value")
    private float billableMax;
    public Set<String> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(Set<String> competencies) {
        this.competencies = competencies;
    }

    public Set<String> getTitles() {
        return titles;
    }

    public void setTitles(Set<String> titles) {
        this.titles = titles;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getIncludeResigned() {
        return includeResigned;
    }

    public void setIncludeResigned(Boolean includeResigned) {
        this.includeResigned = includeResigned;
    }

    public float getNonBillableMin() {
        return nonBillableMin;
    }

    public void setNonBillableMin(float nonBillableMin) {
        this.nonBillableMin = nonBillableMin;
    }

    public float getNonBillableMax() {
        return nonBillableMax;
    }

    public void setNonBillableMax(float nonBillableMax) {
        this.nonBillableMax = nonBillableMax;
    }

    public float getBilliableMin() {
        return billiableMin;
    }

    public void setBilliableMin(float billiableMin) {
        this.billiableMin = billiableMin;
    }

    public float getBillableMax() {
        return billableMax;
    }

    public void setBillableMax(float billableMax) {
        this.billableMax = billableMax;
    }
}
