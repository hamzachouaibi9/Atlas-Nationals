package com.fbla.atlas.atlas.models;

import java.io.Serializable;

/**
 * Created by mentor on 12/5/17.
 */

public class Book implements Serializable{

    String title, description, image, genre, author;


    public Book(String title, String description, String image, String genre, String author) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.author = author;
        this.genre =genre;
    }

    public Book() {
        //empty constructor
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
