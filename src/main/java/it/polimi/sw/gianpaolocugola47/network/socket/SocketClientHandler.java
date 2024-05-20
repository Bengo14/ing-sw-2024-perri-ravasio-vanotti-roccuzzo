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
            line = line();

            switch (line) {

                case "ping" -> {
                    synchronized (this) {this.pingAck = true;}
                }
                case "numPlayers" -> setNumOfPlayers(integer());

                case "addPlayer" -> addPlayer(integer(), line());

                case "drawStarting" -> client.drawStartingCardResponse(drawStartingCard());

                case "setStarting" -> {
                    int id = integer();
                    StartingCard card = (StartingCard) Deck.getCardFromGivenId(integer());
                    if(bool()) {card.switchFrontBack();}
                    client.setStartingCardAndDrawObjectivesResponse(setStartingCardAndDrawObjectives(id, card));
                }
                case "setObj" -> {
                    int id = integer();
                    Objectives obj = Deck.getObjectiveCardFromGivenId(integer());
                    setSecretObjective(id, obj);
                }
                case "play" -> client.playCardResponse(playCard(integer(), integer(), integer(), integer(), integer(), bool()));

                case "draw" -> drawCard(integer(), integer());

                case "getCardsOnHand" -> client.getCardsOnHandResponse(getCardsOnHand());

                case "getPlacedCards" -> client.getPlacedCardsResponse(getPlacedCards(integer()));

                case "getResourceCounter" -> client.getResourceCounterResponse(getResourceCounter(integer()));

                case "message" -> {
                    String sender = line();
                    int id = integer();
                    String msg = line();
                    ChatMessage message = new ChatMessage(sender, id);
                    message.setMessage(msg);
                    sendMessage(message);
                }
                case "privateMessage" -> {
                    String receiver = line();
                    String sender = line();
                    int id = integer();
                    String msg = line();
                    ChatMessage message = new ChatMessage(sender, id);
                    message.setReceiver(receiver);
                    message.setMessage(msg);
                    sendPrivateMessage(message);
                }
                case "nickAvailable" -> client.isNicknameAvailableResponse(isNicknameAvailable(line()));

                case "getNick" -> client.getNicknamesResponse(getNicknames());

                case "getPlayPos" -> client.getPlayablePositionsResponse(getPlayablePositions(integer()));

                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private int integer() throws IOException {
        return Integer.parseInt(line());
    }

    private boolean bool() throws IOException {
        return Boolean.parseBoolean(line());
    }

    private String line() throws IOException {
        return input.readLine();
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

    /* methods of interface VirtualView */

    @Override
    public void terminate() {
        this.client.terminate();
    }

    @Override
    public void ping() {
        this.client.ping();
    }

    @Override
    public void startGame() {
        this.client.startGame();
    }

    @Override
    public void setMyTurn() {
        this.client.setMyTurn();
    }

    @Override
    public void gameOver() {
        this.client.gameOver();
    }

    @Override
    public void showWinner() {
        this.client.showWinner();
    }

    @Override
    public int getId() {
        return this.id; // id saved in local so socket communication is not needed
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        this.client.receiveMessage(message);
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {
        this.client.receivePrivateMessage(message);
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {
        this.client.initView(nicknames, globalObjectives, cardsOnHand, cardsOnTable);
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        this.client.updateDecks(resourceCardOnTop, goldCardOnTop);
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints)  {
        this.client.updatePoints(boardPoints, globalPoints);
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
        synchronized (socketServer) {
            return this.socketServer.getNicknames();
        }
    }
}

