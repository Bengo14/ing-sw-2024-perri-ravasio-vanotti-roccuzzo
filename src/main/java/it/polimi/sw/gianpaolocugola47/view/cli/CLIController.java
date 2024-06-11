package it.polimi.sw.gianpaolocugola47.view.cli;

import it.polimi.sw.gianpaolocugola47.model.*;

public class CLIController {

    private final PlayerTable localPlayerTable;
    private Objectives[] objectives;
    private ResourceCard[] cardsOnTable;
    private GoldCard goldCardOnTop;
    private ResourceCard resourceCardOnTop;
    private int[] globalPoints;
    private int[] boardPoints;
    private String[] nicknames;


    public CLIController(PlayerTable localPlayerTable) {
        this.localPlayerTable = localPlayerTable;
    }

    public PlayerTable getLocalPlayerTable() {
        return localPlayerTable;
    }

    public Objectives[] getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives[] objectives) {
        this.objectives = objectives;
    }

    public ResourceCard[] getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(ResourceCard[] cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public GoldCard getGoldCardOnTop() {
        return goldCardOnTop;
    }

    public void setGoldCardOnTop(GoldCard goldCardOnTop) {
        this.goldCardOnTop = goldCardOnTop;
    }

    public ResourceCard getResourceCardOnTop() {
        return resourceCardOnTop;
    }

    public void setResourceCardOnTop(ResourceCard resourceCardOnTop) {
        this.resourceCardOnTop = resourceCardOnTop;
    }

    public int[] getGlobalPoints() {
        return globalPoints;
    }

    public void setGlobalPoints(int[] globalPoints) {
        this.globalPoints = globalPoints;
    }

    public int[] getBoardPoints() {
        return boardPoints;
    }

    public void setBoardPoints(int[] boardPoints) {
        this.boardPoints = boardPoints;
    }

    public void setNickname(String nickname){
        this.localPlayerTable.setNickname(nickname);
    }

    public String getNickname(){
        return this.localPlayerTable.getNickName();
    }

    public int getId(){
        return this.localPlayerTable.getId();
    }

    public void setNicknames(String[] nicknames){
        this.nicknames = nicknames;
    }

    public String[] getNicknames(){
        return this.nicknames;
    }

    public void initView(Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable){
        this.objectives = globalObjectives;
        this.cardsOnTable = cardsOnTable;
        this.localPlayerTable.setCardsOnHand(cardsOnHand);
    }

    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        if(drawPos == 0 || drawPos == 1) {
            cardsOnTable[drawPos] = this.resourceCardOnTop;
            cardsOnTable[drawPos].switchFrontBack();
        }
        if(drawPos == 2 || drawPos == 3) {
            cardsOnTable[drawPos] = this.goldCardOnTop;
            cardsOnTable[drawPos].switchFrontBack();
        }

        this.resourceCardOnTop = resourceCardOnTop;
        this.goldCardOnTop = goldCardOnTop;
    }

    public void updatePoints(int[] boardPoints, int[] globalPoints){
        this.boardPoints = boardPoints;
        this.globalPoints = globalPoints;
    }

    public StartingCard getStartingCard(){
        return this.localPlayerTable.getStartingCard();
    }

    public Objectives getSecretObjective(){
        return this.localPlayerTable.getSecretObjective();
    }

}
