package model;

public class ChatMessage {
    private String senderId;
    private String receiverId;  // Agregado el campo receiverId para el otro usuario
    private String text;

    public ChatMessage() {
        // Constructor vacío necesario para Firebase
    }

    // Constructor para crear un mensaje con senderId y text
    public ChatMessage(String senderId, String text) {
        this.senderId = senderId;
        this.text = text;
    }

    // Constructor para crear un mensaje con senderId, receiverId y text
    public ChatMessage(String senderId, String receiverId, String text) {
        this.senderId = senderId;
        this.receiverId = receiverId;  // Asignamos receiverId
        this.text = text;
    }

    // Getter y setter para senderId
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Getter y setter para receiverId
    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    // Getter y setter para text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
