package model;

public class ChatMessage {
    private String senderId;
    private String text;

    public ChatMessage() {
        // Constructor vacío necesario para Firebase
    }

    public ChatMessage(String senderId, String text) {
        this.senderId = senderId;
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

