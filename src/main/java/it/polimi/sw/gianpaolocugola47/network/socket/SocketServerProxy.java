package it.polimi.sw.gianpaolocugola47.network.socket;


import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class SocketServerProxy implements VirtualServer {
    private final PrintWriter output;

    public SocketServerProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    public void pingAck() {
        output.println("ping");
        output.flush();
    }

    @Override
    public int connect(VirtualView client) throws RemoteException {
        // not used
        return 0;
    }
    @Override
    public void setNumOfPlayers(int num) {

    }
    @Override
    public void addPlayer(int id, String nickname) {

    }
    @Override
    public StartingCard drawStartingCard() {
        return null;
    }
    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        return new Objectives[0];
    }
    @Override
    public void setSecretObjective(int playerId, Objectives obj) throws RemoteException {

    }
    @Override
    public void turnCardOnHand(int playerId, int position) {

    }
    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) {
        return false;
    }
    @Override
    public void drawCard(int position, int playerId) {

    }
    @Override
    public ResourceCard[][] getCardsOnHand() {
        return new ResourceCard[0][];
    }
    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        return new PlaceableCard[0][];
    }

    @Override
    public int[] getResourceCounter(int playerId) throws RemoteException {
        return new int[0];
    }

    @Override
    public void login() throws RemoteException {

    }
    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {

    }
    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {

    }
    @Override
    public boolean isNicknameAvailable(String nickname, int id) throws RemoteException {
        return false;
    }
    @Override
    public String[] getNicknames() throws RemoteException {
        return new String[0];
    }
    @Override
    public boolean[][] getPlayablePositions(int playerId) throws RemoteException {
        return new boolean[0][];
    }
}

