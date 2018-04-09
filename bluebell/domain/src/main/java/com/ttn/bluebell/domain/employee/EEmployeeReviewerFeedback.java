package com.ttn.bluebell.domain.employee;

import com.ttn.bluebell.domain.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ttn on 2/11/16.
 */

@Entity
@Table(name = "EMPLOYEE_REVIEWER_FEEDBACK")
public class EEmployeeReviewerFeedback extends BaseModel implements Serializable{

    @Id
    @Column(name = "ID")
    @GenericGenerator(name="idGenerator", strategy="increment")
    @GeneratedValue(generator="idGenerator")
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Embedded
    private ERating rating;

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

    public ERating getRating() {
        return rating;
    }

    public void setRating(ERating rating) {
        this.rating = rating;
    }
}
