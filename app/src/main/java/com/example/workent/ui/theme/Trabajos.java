package com.example.workent.ui.theme;

public class Trabajos {

    private String id;
    private String tittle;
    private String description;
    private String price;
    private String user;
    private String imageUri;
    private String startTime; // Hora de inicio
    private String endTime;   // Hora de fin

    public Trabajos() {
    }

    public Trabajos(String tittle, String description, String price, String user, String imageUri, String startTime, String endTime, String id) {
        this.tittle = tittle;
        this.description = description;
        this.price = price;
        this.user = user;
        this.imageUri = imageUri;
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getUser() {
        return user;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getId() {
        return id;
    }
}
