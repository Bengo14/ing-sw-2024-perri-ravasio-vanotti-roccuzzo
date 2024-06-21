package it.polimi.sw.gianpaolocugola47.network.socket;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.network.ChatMessage;

import java.io.BufferedWriter;
import java.io.PrintWriter;

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
    public int connect(VirtualView client) {
        // not used here
        return 0;
    }

    @Override
    public void setNumOfPlayers(int num) {
        output.println("numPlayers");
        output.println(num);
        output.flush();
    }

    @Override
    public void addPlayer(int id, String nickname) {
        output.println("addPlayer");
        output.println(id);
        output.println(nickname);
        output.flush();
    }

    @Override
    public StartingCard drawStartingCard() {
        output.println("drawStarting");
        output.flush();
        return null;
    }

    @Override
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        output.println("setStarting");
        output.println(card.getId());
        output.println(card.getIsFront());
        output.flush();
        return new Objectives[0];
    }

    @Override
    public void setSecretObjective(int playerId, Objectives obj) {
        output.println("setObj");
        output.println(obj.getId());
        output.flush();
    }

    @Override
    public void startGameFromFile() {
        output.println("startFromFile");
        output.flush();
    }

    @Override
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        output.println("play");
        output.println(onHandCard);
        output.println(onTableCardX);
        output.println(onTableCardY);
        output.println(onTableCardCorner);
        output.println(playerId);
        output.println(isFront);
        output.flush();
        return false;
    }

    @Override
    public void drawCard(int position, int playerId) {
        output.println("draw");
        output.println(position);
        output.println(playerId);
        output.flush();
    }

    @Override
    public ResourceCard[][] getCardsOnHand() {
        output.println("getCardsOnHand");
        output.flush();
        return new ResourceCard[0][];
    }

    @Override
    public PlaceableCard[][] getPlacedCards(int playerId) {
        output.println("getPlacedCards");
        output.println(playerId);
        output.flush();
        return new PlaceableCard[0][];
    }

    @Override
    public Objectives getSecretObjective(int playerId) {
        output.println("getSecretObj");
        output.println(playerId);
        output.flush();
        return null;
    }

    @Override
    public int[] getResourceCounter(int playerId) {
        output.println("getResourceCounter");
        output.println(playerId);
        output.flush();
        return new int[0];
    }

    @Override
    public void sendMessage(ChatMessage message) {
        output.println("message");
        output.println(message.getSender());
        output.println(message.getSenderId());
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public void sendPrivateMessage(ChatMessage message) {
        output.println("privateMessage");
        output.println(message.getReceiver());
        output.println(message.getSender());
        output.println(message.getSenderId());
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public boolean isNicknameAvailable(String nickname) {
        output.println("nickAvailable");
        output.println(nickname);
        output.flush();
        return false;
    }

    @Override
    public String[] getNicknames() {
        output.println("getNick");
        output.flush();
        return new String[0];
    }

    @Override
    public int getNumOfPlayers() {
        output.println("getNumPlayers");
        output.flush();
        return 0;
    }

    @Override
    public boolean[][] getPlayablePositions(int playerId) {
        output.println("getPlayPos");
        output.println(playerId);
        output.flush();
        return new boolean[0][];
    }
}