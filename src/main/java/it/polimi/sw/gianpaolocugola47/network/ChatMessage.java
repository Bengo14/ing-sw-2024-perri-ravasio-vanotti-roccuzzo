package it.polimi.sw.gianpaolocugola47.network;

import java.io.Serializable;

/**
 * This class manages the messages exchanged between clients and the server.
 * It contains the message, the sender, the receiver, the sender's id,
 * the receiver's id and a boolean that indicates if the message is private.
 */
public class ChatMessage implements Serializable {

    private String message;
    private final String  sender;
    private String receiver;
    private boolean isPrivate;
    private final int senderId;
    private int receiverId;

    /**
     * Constructor for a private message.
     * @param message the message to send
     * @param sender the name of the sender
     * @param receiver the name of the receiver
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     */
    public ChatMessage(String message, String sender, String receiver, int senderId, int receiverId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sender = sender;
        this.receiver = receiver;
        this.isPrivate = false;
    }
    /**
     * Constructor for a public message.
     * @param sender the name of the sender
     * @param senderId the id of the sender
     */
    public ChatMessage(String sender, int senderId){
        this.sender = sender;
        this.senderId = senderId;
        this.receiver = null;
        this.isPrivate = false;
    }

    /**
     * This method returns the message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * This method returns the sender.
     * @return the sender
     */
    public String getSender() {
        return sender;
    }
    /**
     * This method returns the receiver.
     * @return the receiver
     */
    public String getReceiver() {
        return receiver;
    }
    /**
     * This method sets the receiver.
     * @param receiver the receiver
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    /**
     * This method sets the message.
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * This method returns if the message is private.
     * @return true if the message is private, false otherwise
     */
    public boolean isPrivate() {
        return isPrivate;
    }
    /**
     * This method sets if the message is private.
     * @param isPrivate true if the message is private, false otherwise
     */
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    /**
     * This method returns the sender's id.
     * @return the sender's id
     */
    public int getSenderId() { return senderId;}
    /**
     * This method returns the receiver's id.
     * @return the receiver's id
     */
    public int getReceiverId() { return receiverId; }
}