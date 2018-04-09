package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.durable.model.employee.Competency;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by ttnd on 5/10/16.
 */
@Embeddable
public class ECompetency implements Serializable{

    @NotNull
    @Column(name = "COMPETENCY_NAME")
    private String name;

    @NotNull
    @Column(name = "COMPETENCY_RATING")
    @Enumerated(EnumType.STRING)
    private Competency.Rating rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Competency.Rating getRating() {
        return rating;
    }

    public void setRating(Competency.Rating rating) {
        this.rating = rating;
    }
}
