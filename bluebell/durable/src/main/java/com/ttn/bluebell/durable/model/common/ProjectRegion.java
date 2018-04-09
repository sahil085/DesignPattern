package com.ttn.bluebell.durable.model.common;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class ProjectRegion {
    @ApiModelProperty(notes = "Project Region")
    private Long id;
    @ApiModelProperty(notes = "Region Name")
    private String regionName;
    @ApiModelProperty(notes = "Not updatable Field(Newers World ID)")
    private String externalId;

    public ProjectRegion() {
    }

    public ProjectRegion(Long id, String regionName, String externalId) {
        this.id = id;
        this.regionName = regionName;
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
