package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

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

                case "drawStarting" -> {
                    synchronized (client) {
                        client.drawStartingCardResponse(drawStartingCard());
                    }
                }

                case "setStarting" -> {
                    int id = integer();
                    StartingCard card = (StartingCard) Deck.getCardFromGivenId(integer());
                    if(bool()) {card.switchFrontBack();}
                    synchronized (client) {
                        client.setStartingCardAndDrawObjectivesResponse(setStartingCardAndDrawObjectives(id, card));
                    }
                }

                case "setObj" -> {
                    int id = integer();
                    Objectives obj = Deck.getObjectiveCardFromGivenId(integer());
                    setSecretObjective(id, obj);
                }

                case "play" -> {
                    synchronized (client) {
                        client.playCardResponse(playCard(integer(), integer(), integer(), integer(), integer(), bool()));
                    }
                }

                case "draw" -> drawCard(integer(), integer());

                case "getCardsOnHand" -> {
                    synchronized (client) {
                        client.getCardsOnHandResponse(getCardsOnHand());
                    }
                }

                case "getPlacedCards" -> {
                    synchronized (client) {
                        client.getPlacedCardsResponse(getPlacedCards(integer()));
                    }
                }

                case "getResourceCounter" -> {
                    synchronized (client) {
                        client.getResourceCounterResponse(getResourceCounter(integer()));
                    }
                }

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

                case "nickAvailable" -> {
                    synchronized (client) {
                        client.isNicknameAvailableResponse(isNicknameAvailable(line()));
                    }
                }

                case "getNick" -> {
                    synchronized (client) {
                        client.getNicknamesResponse(getNicknames());
                    }
                }

                case "getNumPlayers" -> {
                    synchronized (client) {
                        client.getNumOfPlayersResponse(getNumOfPlayers());
                    }
                }

                case "getPlayPos" -> {
                    synchronized (client) {
                        client.getPlayablePositionsResponse(getPlayablePositions(integer()));
                    }
                }

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
    public void startGame() {
        synchronized (client) {
            this.client.startGame();
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

