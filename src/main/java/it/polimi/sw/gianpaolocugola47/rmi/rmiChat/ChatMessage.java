package it.polimi.sw.gianpaolocugola47.rmi.rmiChat;

public class ChatMessage {
    private String message;
    private final String  sender;
    private String receiver;
    private boolean isPrivate;

    public ChatMessage(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.isPrivate = false;
    }

    public ChatMessage(String sender){
        this.sender = sender;
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
}
