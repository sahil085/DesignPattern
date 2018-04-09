package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;
import com.ttn.bluebell.domain.region.Region;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ttnd on 21/10/16.
 */
@Entity
@Table(name = "EMPLOYEE_REGIONS")
public class EEmployeeRegions extends BaseModel {

    @Id
    @Column(name = "ID")
    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "REGION", nullable = false)
    private String region;

    @OneToOne
    @JoinColumn(name = "REGION_ID")
    private Region regionvalue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Region getRegionvalue() {
        return regionvalue;
    }

    public void setRegionvalue(Region regionvalue) {
        this.regionvalue = regionvalue;
    }
}


