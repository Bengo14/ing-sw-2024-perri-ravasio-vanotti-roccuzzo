package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClientHandler implements VirtualView, VirtualServer {
    private final Controller controller;
    private final SocketServer socketServer;
    protected final SocketClientProxy client;
    private final Socket socket;
    protected int id;
    protected boolean pingAck = true;

    public SocketClientHandler(Controller controller, SocketServer socketServer, Socket socket) throws IOException {
        this.controller = controller;
        this.socketServer = socketServer;
        this.socket = socket;
        this.client = new SocketClientProxy(socket.getOutputStream());
    }

    public void runVirtualView() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        while (true) {
            SocketMessage message = (SocketMessage) ois.readObject();
            message.doAction(this);
        }
    }

    protected void setId(int id) {
        this.id = id;
        this.client.setId(id);
    }

    protected synchronized boolean getPingAck() {
         boolean ping = pingAck;
         pingAck = false;
         return ping;
    }

    protected synchronized void setPingAck() {
        pingAck = true;
    }

    /* methods of interface VirtualView */

    @Override
    public void terminate() {
        synchronized (client) {
            this.client.terminate();
        }
    }

    @Override
    public void ping() {
        synchronized (client) {
            this.client.ping();
        }
    }

    @Override
    public void startGame(boolean isLoaded) {
        synchronized (client) {
            this.client.startGame(isLoaded);
        }
    }

    @Override
    public void setMyTurn() {
        synchronized (client) {
            this.client.setMyTurn();
        }
    }

    @Override
    public void gameOver() {
        synchronized (client) {
            this.client.gameOver();
        }
    }

    @Override
    public void showWinner(int id) {
        synchronized (client) {
            this.client.showWinner(id);
        }
    }

    @Override
    public int getId() {
        return this.id; // id saved in local so socket communication is not needed
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        synchronized (client) {
            client.receiveMessage(message);
        }
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {
        synchronized (client) {
            client.receivePrivateMessage(message);
        }
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        synchronized (client) {
            client.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
        }
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        synchronized (client) {
            client.updateDecks(resourceCardOnTop, goldCardOnTop, drawPos);
        }
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints)  {
        synchronized (client) {
            client.updatePoints(boardPoints, globalPoints);
        }
    }

    /* methods of interface VirtualServer */

    @Override
    public int connect(VirtualView client) throws RemoteException {
        return -1; // not used here
    }

    @Override
    public void setNumOfPlayers(int num) {
        synchronized (controller) {
            this.controller.setNumOfPlayers(num);
        }
    }

    @Override
    public void addPlayer(int id, String nickname) {
        synchronized (controller) {
            System.out.println(STR."Player \{nickname} added with id \{id}");
            this.controller.addPlayer(id, nickname);
        }
    }

    @Override
    public StartingCard drawStartingCard() {
        synchronized (controller) {
            return this.controller.drawStartingCard();
        }
    }

    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        synchronized (controller) {
            return this.controller.setStartingCardAndDrawObjectives(playerId, card);
        }
    }

    @Override
    public void setSecretObjective(int playerId, Objectives obj) {
        synchronized (controller) {
            this.controller.setSecretObjectiveAndUpdateView(playerId, obj);
        }
    }

    @Override
    public void startGameFromFile() {
        synchronized (controller) {
            this.controller.startGameFromFile();
        }
    }

    @Override
    public boolean[][] getPlayablePositions(int playerId) {
        synchronized (controller) {
            return this.controller.getPlayablePositions(playerId);
        }
    }

    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        synchronized (controller) {
            return controller.playCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId, isFront);
        }
    }

    @Override
    public void drawCard(int position, int playerId) {
        synchronized (controller) {
            controller.drawCard(position, playerId);
        }
    }

    @Override
    public ResourceCard[][] getCardsOnHand() {
        synchronized (controller) {
            return controller.getCardsOnHand();
        }
    }

    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        synchronized (controller) {
            return controller.getPlacedCards(playerId);
        }
    }

    @Override
    public Objectives getSecretObjective(int playerId) {
        synchronized (controller) {
            return controller.getSecretObjective(playerId);
        }
    }

    @Override
    public int[] getResourceCounter(int playerId)  {
        synchronized (controller) {
            return controller.getResourceCounter(playerId);
        }
    }

    @Override
    public void sendMessage(ChatMessage message)  {
        synchronized (this.socketServer) {
            this.socketServer.sendMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage message)  {
        synchronized (this.socketServer) {
            this.socketServer.sendPrivateMessage(message);
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname)  {
        String [] nicknames = getNicknames();
        for (String nick : nicknames)
            if (nick.equals(nickname))
                return false;
        return true;
    }

    @Override
    public String[] getNicknames()  {
        synchronized (controller) {
            return this.controller.getNicknames();
        }
    }

    @Override
    public int getNumOfPlayers() {
        synchronized (controller) {
            return this.controller.getNumOfPlayers();
        }
    }
}