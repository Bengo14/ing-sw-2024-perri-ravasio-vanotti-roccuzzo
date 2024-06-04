package it.polimi.sw.gianpaolocugola47.network.socket;


import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Arrays;

public class SocketClientProxy implements VirtualView {
    private final PrintWriter output;

    public SocketClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    protected void setId(int id) {
        output.println("setId");
        output.println(id);
        output.flush();
    }

    @Override
    public void terminate() {
        output.println("terminate");
        output.flush();
    }

    @Override
    public void ping() {
        output.println("ping");
        output.flush();
    }

    @Override
    public void startGame() {
        output.println("start");
        output.flush();
    }

    @Override
    public void setMyTurn() {
        output.println("turn");
        output.flush();
    }

    @Override
    public void gameOver() {
        output.println("gameOver");
        output.flush();
    }

    @Override
    public void showWinner() {
        output.println("winner");
        output.flush();
    }

    @Override
    public int getId() {
        return -1; // not used here
    }

    @Override
    public void receiveMessage(ChatMessage message) {
        output.println("message");
        output.println(message.getSender());
        output.println(message.getSenderId());
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {
        output.println("privateMessage");
        output.println(message.getSender());
        output.println(message.getSenderId());
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {

        output.println("init");

        Arrays.stream(nicknames).forEachOrdered(output::println);
        Arrays.stream(globalObjectives).mapToInt(this::getObjectiveId).forEachOrdered(output::println);
        Arrays.stream(cardsOnHand).mapToInt(this::getCardId).forEachOrdered(output::println);
        Arrays.stream(cardsOnTable).mapToInt(this::getCardId).forEachOrdered(output::println);

        output.flush();
    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {
        output.println("decks");
        output.println(getCardId(resourceCardOnTop));
        output.println(getCardId(goldCardOnTop));
        output.flush();
    }

    @Override
    public void updatePoints(int[] boardPoints, int[] globalPoints) {
        output.println("points");
        Arrays.stream(boardPoints).forEachOrdered(output::println);
        Arrays.stream(globalPoints).forEachOrdered(output::println);
        output.flush();
    }

    protected void drawStartingCardResponse(StartingCard card) {
        output.println("drawStarting");
        output.println(getCardId(card));
        output.flush();
    }

    protected void setStartingCardAndDrawObjectivesResponse(Objectives[] obj) {
        output.println("setStarting");
        output.println(getObjectiveId(obj[0]));
        output.println(getObjectiveId(obj[1]));
        output.flush();
    }

    protected void playCardResponse(boolean bool) {
        output.println("play");
        output.println(bool);
        output.flush();
    }

    protected void getCardsOnHandResponse(ResourceCard[][] cardsOnHand) {
        output.println("getCardsOnHand");
        for(int i=0; i<cardsOnHand.length; i++)
            Arrays.stream(cardsOnHand[i]).mapToInt(this::getCardId).forEachOrdered(output::println);
        output.flush();
    }

    protected void getPlacedCardsResponse(PlaceableCard[][] placedCards) {
        output.println("getPlacedCards");
        for(int i=0; i<placedCards.length; i++)
            Arrays.stream(placedCards[i]).mapToInt(this::getCardId).forEachOrdered(output::println);
        output.flush();
    }

    protected void getResourceCounterResponse(int[] resourceCounter) {
        output.println("getResourceCounter");
        Arrays.stream(resourceCounter).forEachOrdered(output::println);
        output.flush();
    }

    protected void isNicknameAvailableResponse(boolean bool) {
        output.println("nickAvailable");
        output.println(bool);
        output.flush();
    }

    protected void getNicknamesResponse(String[] nicknames) {
        output.println("getNick");
        output.println(nicknames.length); //num of players
        Arrays.stream(nicknames).forEachOrdered(output::println);
        output.flush();
    }

    protected void getNumOfPlayersResponse(int numOfPlayers) {
        output.println("getNumPlayers");
        output.println(numOfPlayers);
        output.flush();
    }

    protected void getPlayablePositionsResponse(boolean[][] playablePositions) {
        output.println("getPlayPos");
        for(int i=0; i<playablePositions.length; i++)
            for(int j=0; j<playablePositions.length; j++)
                output.println(playablePositions[i][j]);
        output.flush();
    }

    private int getCardId(PlaceableCard card) {
        return card.getId();
    }
    private int getObjectiveId(Objectives obj) {
        return obj.getId();
    }
}
