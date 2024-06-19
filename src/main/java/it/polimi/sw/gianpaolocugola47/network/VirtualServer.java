package it.polimi.sw.gianpaolocugola47.network;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    int connect(VirtualView client) throws RemoteException;
    void setNumOfPlayers(int num) throws RemoteException;
    void addPlayer(int id, String nickname) throws RemoteException;
    StartingCard drawStartingCard() throws RemoteException;
    Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) throws RemoteException;
    void setSecretObjective(int playerId, Objectives obj) throws RemoteException;
    void startGameFromFile() throws RemoteException;
    boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) throws RemoteException;
    void drawCard(int position, int playerId) throws RemoteException;
    ResourceCard[][] getCardsOnHand() throws RemoteException;
    PlaceableCard[][] getPlacedCards(int playerId) throws RemoteException;
    Objectives getSecretObjective(int playerId) throws RemoteException;
    int[] getResourceCounter(int playerId) throws RemoteException;
    void sendMessage(ChatMessage message) throws RemoteException;
    void sendPrivateMessage(ChatMessage message) throws RemoteException;
    boolean isNicknameAvailable(String nickname) throws RemoteException;
    String[] getNicknames() throws RemoteException;
    int getNumOfPlayers() throws RemoteException;
    boolean[][] getPlayablePositions(int playerId) throws RemoteException;

}