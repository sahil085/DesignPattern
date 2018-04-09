package com.ttn.bluebell.domain.common;

import com.ttn.bluebell.domain.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ttnd on 25/10/16.
 */
@Entity
@Table(name = "SYSTEM_PARAMETER")
public class ESystemParameter extends BaseModel {

    @Id
    @Column(name = "PARAMETER_ID")
    @GenericGenerator(name="idGenerator", strategy="increment")
    @GeneratedValue(generator="idGenerator")
    protected Long parameterId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "PARAMETER_TYPE")
    @NotNull
    private String type;

    @Column(name = "DESCRIPTION")
    private String description;

    public ESystemParameter() {
    }

    public ESystemParameter(String name, String value, String type, String description) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.description = description;
    }

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

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
