package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    int connect(VirtualView client) throws RemoteException;
    void setNumOfPlayers(int num) throws RemoteException;
    void addPlayer(int id, String nickname) throws RemoteException;
    void setStartingCard(int playerId, StartingCard card) throws RemoteException;
    void setSecretObjective(int playerId, Objectives obj) throws RemoteException;
    void login() throws RemoteException;
    void sendMessage(ChatMessage message) throws RemoteException;
    void sendPrivateMessage(ChatMessage message) throws RemoteException;
    boolean isNicknameAvailable(String nickname, int id) throws RemoteException;
    String[] getNicknames() throws RemoteException;
}
