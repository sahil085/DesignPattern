package com.ttn.bluebell.durable.model.employee;

import java.io.Serializable;

/**
 * Created by praveshsaini on 4/10/16.
 */
public class ReviewerFeedback implements Serializable{
    private Long id;
    private String email;
    private Rating rating;

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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
