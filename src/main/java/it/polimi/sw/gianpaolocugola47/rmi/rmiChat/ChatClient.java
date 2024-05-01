package it.polimi.sw.gianpaolocugola47.rmi.rmiChat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    String getNickname() throws RemoteException;
    int getId() throws RemoteException;
    void receiveMessage(ChatMessage message) throws RemoteException;
    void receivePrivateMessage(ChatMessage message) throws RemoteException;
}
