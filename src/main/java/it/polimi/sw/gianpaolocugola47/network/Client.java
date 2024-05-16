package it.polimi.sw.gianpaolocugola47.network;

import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

public interface Client {

    int getBoardPoints();

    void terminateLocal();

    StartingCard drawStartingCard();

    Objectives[] setStartingCardAndDrawObjectives();

    void setSecretObjective();

    boolean[][] getPlayablePositions();

    boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner);

    void drawCard(int position);

    ResourceCard[][] getCardsOnHand();

    PlaceableCard[][] getPlacedCards(int playerId);

    int[] getResourceCounter(int playerId);

    int getGlobalPoints();

    int getIdLocal();

    String getNicknameLocal();

    String[] getNicknames();

    void sendMessage(ChatMessage msg);

    void sendPrivateMessage(ChatMessage msg);

    boolean isItMyTurn();
}
