package com.example.workent;

public class ChatRoom {

    private String sender;
    private String receiver;

    private String text,imageUrl;

    public ChatRoom(String sender, String receiver, String text,String imageUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.imageUrl=imageUrl;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}



