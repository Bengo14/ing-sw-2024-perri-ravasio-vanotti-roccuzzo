package it.polimi.sw.gianpaolocugola47.rmi;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String message;
    private final String  sender;
    private String receiver;
    private boolean isPrivate;
    private final int senderId;
    private int receiverId;

    public ChatMessage(String message, String sender, String receiver, int senderId, int receiverId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sender = sender;
        this.receiver = receiver;
        this.isPrivate = false;
    }

    public ChatMessage(String sender, int senderId){
        this.sender = sender;
        this.senderId = senderId;
        this.receiver = null;
        this.isPrivate = false;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getSenderId() { return senderId;}

    public int getReceiverId() { return receiverId; }
}
