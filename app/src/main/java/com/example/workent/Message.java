package com.example.workent;

import java.util.Date;

public class Message {
    private String text;
    private String sender;
    private String receiver;
    private Date timestamp;
    private String imageUrl;

    // Constructor vacío requerido para Firestore
    public Message() {
    }

    // Constructor para mensajes (puede ser con texto o con imagen)
    public Message(String text, String imageUrl, String sender, String receiver, Date timestamp) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
