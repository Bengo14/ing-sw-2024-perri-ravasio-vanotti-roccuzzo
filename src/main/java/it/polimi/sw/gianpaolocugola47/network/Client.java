package it.polimi.sw.gianpaolocugola47.network;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;

import java.util.ArrayList;

/**
 * This interface is used to define the methods that the client must implement.
 * The client is the player that is connected to the server and that can play the game.
 */
public interface Client {

    void terminateLocal();
    StartingCard drawStartingCard();
    Objectives[] setStartingCardAndDrawObjectives();
    void setSecretObjective();
    void startGameFromFile();
    boolean[][] getPlayablePositions();
    Objectives getSecretObjective();
    boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, boolean isFront);
    void drawCard(int position);
    ResourceCard[][] getCardsOnHand();
    PlaceableCard[][] getPlacedCards(int playerId);
    int[] getResourceCounter(int playerId);
    int getIdLocal();
    String getNicknameLocal();
    String[] getNicknames();
    void sendMessage(ChatMessage msg);
    void sendPrivateMessage(ChatMessage msg);
    boolean isItMyTurn();
    ArrayList<int[]> getPlacingOrder(int id);
}