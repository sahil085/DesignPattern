package com.ttn.bluebell.domain.region;

import com.ttn.bluebell.domain.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "REGION")
public class Region extends BaseModel {
    @Id
    @Column(name = "ID")
    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    @Column(name = "REGION_NAME", nullable = false)
    private String regionName;
    @Column(name = "EXTERNAL_ID", nullable = false, unique = true)
    private String externalId;
    @Column(name = "STATUS")
    private Boolean status;

    public Region() {

    }

    public Region(String regionName, String externalId, Boolean status) {
        this.regionName = regionName;
        this.externalId = externalId;
        this.status = status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
