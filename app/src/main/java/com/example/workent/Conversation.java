package com.example.workent;

// Conversation.java
import java.util.List;

public class Conversation {
    private List<Message> messages;
    private String sender;
    private String receiver;

    // Constructor, getters y setters
    public Conversation() {
    }

    public Conversation(List<Message> messages, String sender, String receiver) {
        this.messages = messages;
        this.sender = sender;
        this.receiver = receiver;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
}

