package com.fbla.atlas.atlas.models;

import android.support.design.widget.TextInputEditText;
import android.widget.Spinner;

/**
 * Created by Hamza on 1/30/2018.
 */

public class AdminBook {


    String title, description, image, genre, author, stitle;


    public AdminBook(String title, String stitle,String description, String image, String genre, String author) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.author = author;
        this.genre =genre;
    }

    public AdminBook() {
    }

    public AdminBook(String title, String sTitle, String description, String imageURL, Spinner genre, TextInputEditText author) {
        //empty constructor
    }

    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setImage(String image) {
        this.image = image;
    }

}
