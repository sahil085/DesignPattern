package com.ttn.bluebell.durable.model.employee;

import java.io.Serializable;

/**
 * Created by praveshsaini on 29/9/16.
 */
public class Competency implements Serializable{

    public enum Rating{
        Expert(1),
        Intermediate(2),
        Beginner(3);

        Integer ratingValue;
        Rating(Integer ratingValue) {
            this.ratingValue = ratingValue;
        }

        public Integer getRatingValue() {
            return ratingValue;
        }
    }

    private String name;
    private Rating rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
