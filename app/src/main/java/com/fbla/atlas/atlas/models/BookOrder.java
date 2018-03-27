package com.fbla.atlas.atlas.models;

/**
 * Created by Hamza on 1/31/2018.
 */

public class BookOrder {

    String title,image,author,description,genre,date,type;

    public BookOrder(String title, String image, String author, String description, String genre, String date, String type) {
        this.title = title;
        this.image = image;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.date = date;
        this.type = type;
    }

    public BookOrder() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
