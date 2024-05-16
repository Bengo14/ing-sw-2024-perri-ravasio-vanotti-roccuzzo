package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView, VirtualServer {
    private final Controller controller;
    private final SocketServer socketServer;
    private final BufferedReader input;
    private final SocketClientProxy client;
    private int id;
    private boolean pingAck = false;

    public SocketClientHandler(Controller controller, SocketServer socketServer, BufferedReader input, BufferedWriter output) {
        this.controller = controller;
        this.socketServer = socketServer;
        this.input = input;
        this.client = new SocketClientProxy(output);
    }

    public void runVirtualView() throws IOException {
        String line;

        while (true) {
            line = input.readLine();

            switch (line) {
                case "ping" -> {
                    synchronized (this) {this.pingAck = true;}
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }
    protected void setId(int id) {
        this.client.setId(id);
        this.id = id;
    }
    protected synchronized boolean getPingAck() {
         boolean ping = pingAck;
         pingAck = false;
         return ping;
    }

    @Override
    public void terminate() {
        this.client.terminate();
    }

    @Override
    public void ping() {
        this.client.ping();
    }

    @Override
    public void startGame() throws RemoteException {

    }

    @Override
    public void setMyTurn() throws RemoteException {

    }

    @Override
    public void gameOver() {

    }

    @Override
    public void showWinner() throws RemoteException {

    }

    @Override
    public String getNickname() throws RemoteException {
        return null;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void receiveMessage(ChatMessage message) throws RemoteException {

    }

    @Override
    public void receivePrivateMessage(ChatMessage message) throws RemoteException {

    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException {

    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) throws RemoteException {

    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException {

    }

    @Override
    public void setNotMyTurn() throws RemoteException {

    }

    /* methods of interface VirtualServer */

    @Override
    public int connect(VirtualView client) throws RemoteException {
        // not used
        return 0;
    }

    @Override
    public void setNumOfPlayers(int num) throws RemoteException {

    }

    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {

    }

    @Override
    public StartingCard drawStartingCard() throws RemoteException {
        return null;
    }

    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) throws RemoteException {
        return new Objectives[0];
    }

    @Override
    public void setSecretObjective(int playerId, Objectives obj) throws RemoteException {

    }

    @Override
    public void turnCardOnHand(int playerId, int position) throws RemoteException {

    }

    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) throws RemoteException {
        return false;
    }

    @Override
    public void drawCard(int position, int playerId) throws RemoteException {

    }

    @Override
    public ResourceCard[][] getCardsOnHand() throws RemoteException {
        return new ResourceCard[0][];
    }

    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) throws RemoteException {
        return new PlaceableCard[0][];
    }

    @Override
    public int[] getResourceCounter(int playerId) throws RemoteException {
        return new int[0];
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

