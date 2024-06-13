package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

public interface View {

    void setClient(Client client);
    void start();
    void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable);
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos);
    void updatePoints(int[] boardPoints, int[] globalPoints);
    void showTurn();
    void receiveMessage(ChatMessage message);
    void gameOver();
    void showWinner(int id);

    StartingCard getStartingCard();

    Objectives getSecretObjective();
}
