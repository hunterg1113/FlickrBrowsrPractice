package com.example.huntergreer.flickrbrowsrpractice;

import java.io.Serializable;

public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String title;
    private String image;
    private String dateTaken;
    private String description;
    private String author;
    private String tags;

    Photo(String title, String image, String dateTaken, String description, String author, String tags) {
        this.title = title;
        this.image = image;
        this.dateTaken = dateTaken;
        this.description = description;
        this.author = author;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
