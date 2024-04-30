package it.polimi.sw.gianpaolocugola47.rmi.rmiChat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void login(ChatClient client) throws RemoteException;
    void logout(ChatClient client) throws RemoteException;
    void sendMessage(ChatMessage message) throws RemoteException;
    void sendPrivateMessage(ChatMessage message) throws RemoteException;
}
