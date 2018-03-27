package com.fbla.atlas.atlas.models;

/**
 * Created by hamza-chouaibi9 on 3/20/18.
 */

public class Review {

    String comment, date, name, image;
    String rating;

    public Review(String rating, String comment, String date, String name, String image) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.name = name;
        this.image = image;
    }

    public Review() {
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
