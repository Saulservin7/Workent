package com.example.workent;

import android.graphics.Bitmap;

public class ListWork {
    public String tittle,description,price;


    public Bitmap image;


    public ListWork(String tittle, String description, String price, Bitmap image) {
        this.tittle = tittle;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
