package com.fbla.atlas.atlas.models;

/**
 * Created by hamza_chouaibi9 on 1/8/18.
 */

public class Genre {

    String image, title;

    public Genre(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public Genre() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
