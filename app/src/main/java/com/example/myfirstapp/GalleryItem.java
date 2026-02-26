package com.example.myfirstapp;
public class GalleryItem {
    private String title;
    private String description;
    private String imageFileName;

    public GalleryItem(String title, String description, String imageFileName) {
        this.title = title;
        this.description = description;
        this.imageFileName = imageFileName;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageFileName() { return imageFileName; }
}