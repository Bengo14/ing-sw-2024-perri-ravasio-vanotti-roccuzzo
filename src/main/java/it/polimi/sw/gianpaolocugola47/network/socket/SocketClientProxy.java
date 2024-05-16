package it.polimi.sw.gianpaolocugola47.network.socket;


import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.VirtualView;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;

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
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public void receivePrivateMessage(ChatMessage message) {
        output.println("privateMessage");
        output.println(message.getSender());
        output.println(message.getMessage());
        output.flush();
    }

    @Override
    public void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {

        output.println("nicknames");
        for (String nickname : nicknames)
            output.println(nickname);

        output.println("objectives");
        for (Objectives obj : globalObjectives)
            output.println(getObjectiveId(obj));

        output.println("cardsOnHand");
        for (ResourceCard card : cardsOnHand)
            output.println(getCardId(card));

        output.println("cardsOnTable");
        for (ResourceCard card : cardsOnTable)
            output.println(getCardId(card));

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
        for (int i : boardPoints)
            output.println(i);
        for (int i : globalPoints)
            output.println(i);
        output.flush();
    }

    protected void drawStartingCardResponse(StartingCard card) {
        /*todo responses*/
    }

    protected void setStartingCardAndDrawObjectivesResponse(Objectives[] obj) {

    }

    protected void playCardResponse(boolean bool) {

    }

    protected void getCardsOnHandResponse(ResourceCard[][] cardsOnHand) {

    }

    protected void getPlacedCardsResponse(PlaceableCard[][] placedCards) {

    }

    protected void getResourceCounterResponse(int[] resourceCounter) {

    }

    protected void isNicknameAvailableResponse(boolean bool) {

    }

    protected void getNicknamesResponse(String[] nicknames) {

    }

    protected void getPlayablePositionsResponse(boolean[][] playablePositions) {

    }

    private int getCardId(PlaceableCard card) {
        return card.getId();
    }
    private int getObjectiveId(Objectives obj) {
        return obj.getId();
    }
}
