package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;

public class GUI implements View {

    private final Client client;

    public GUI(Client client) {
        this.client = client;
    }
    @Override
    public void start() {

    }
    @Override
    public void setId(int id) {

    }
    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public void initView(String nickname, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) {

    }

    @Override
    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) {

    }

    @Override
    public void updatePoints(int boardPoints, int globalPoints) {

    }

    @Override
    public StartingCard getStartingCard() {
        return null;
    }

    @Override
    public Objectives getSecretObjective() {
        return null;
    }

    @Override
    public int getGlobalPoints() {
        return 0;
    }

    @Override
    public int getBoardPoints() {
        return 0;
    }

}
