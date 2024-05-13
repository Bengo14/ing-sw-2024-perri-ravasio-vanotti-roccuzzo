package it.polimi.sw.gianpaolocugola47.network;

import it.polimi.sw.gianpaolocugola47.model.PlaceableCard;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;

public interface Client {

    int getBoardPoints();

    void drawStartingCard();

    void setStartingCardAndDrawObjectives();

    void setSecretObjective();

    boolean[][] getPlayablePositions();

    void turnCardOnHand(int position);

    boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner);

    void drawCard(int position);

    ResourceCard[][] getCardsOnHand();

    PlaceableCard[][] getPlacedCards(int playerId);

    int[] getResourceCounter(int playerId);

    int getGlobalPoints();

}
