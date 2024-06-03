package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketClient;
import javafx.stage.Stage;

public interface View {

    void start();



    void setId(int id);
    void setNickname(String nickname);
    void initView(String nickname, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable);
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop);
    void updatePoints(int boardPoints, int globalPoints);
    StartingCard getStartingCard();
    Objectives getSecretObjective();
    int getGlobalPoints();
    int getBoardPoints();
    void setClient(Client client);
    void showTurn();
}
