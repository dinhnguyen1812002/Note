package com.example.note.entity;

import java.io.Serializable;

public class Note  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String datetime;
    private String subtitle;
    private String noteText;
    private String imagePath;
    private String color;
    private String webLink;

    public Note(String title, String subtitle, String noteText) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteText = noteText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Note() {
    }

    public Note(String title, String datetime, String noteText, String color, String imagePath) {
        this.title = title;
        this.datetime = datetime;
        this.noteText = noteText;
        this.color = color;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
