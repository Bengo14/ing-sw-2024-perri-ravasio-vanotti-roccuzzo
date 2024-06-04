package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;

import java.io.IOException;

public interface View {

    void start();
    void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws IOException;
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop);
    void updatePoints(int[] boardPoints, int[] globalPoints);
    StartingCard getStartingCard();
    Objectives getSecretObjective();
    int[] getGlobalPoints();
    int[] getBoardPoints();
    void showTurn();

}
