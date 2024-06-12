package it.polimi.sw.gianpaolocugola47.view.cli;

import it.polimi.sw.gianpaolocugola47.model.*;

import java.util.ArrayList;

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

    public void initView(Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable, String[] nicknames){
        this.objectives = globalObjectives;
        this.cardsOnTable = cardsOnTable;
        this.localPlayerTable.setCardsOnHand(cardsOnHand);
        this.nicknames = nicknames;
    }

    public void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) {
        if(drawPos == 0 || drawPos == 1) {
            cardsOnTable[drawPos] = this.resourceCardOnTop;
            cardsOnTable[drawPos].setFront(true);
        }
        if(drawPos == 2 || drawPos == 3) {
            cardsOnTable[drawPos] = this.goldCardOnTop;
            cardsOnTable[drawPos].setFront(true);
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

    public ArrayList<Integer[]> getAvailablePositions(boolean[][] playablePositions){
        ArrayList<Integer[]> availablePositions = new ArrayList<>();
        for(int i = 0; i < playablePositions.length; i++){
            for(int j = 0; j < playablePositions[i].length; j++){
                if(playablePositions[i][j]){
                    Integer[] coordinates = {i,j};
                    availablePositions.add(coordinates);
                }
            }
        }
        return availablePositions;
    }

    public boolean checkIfValidPosition(int x, int y, boolean[][] playablePositions){
        for(Integer[] coordinates : this.getAvailablePositions(playablePositions)){
            if(coordinates[0] == x && coordinates[1] == y)
                return true;
        }
        return false;
    }

    public int getCorner(int x, int y, PlaceableCard[][] localBoard){ //TO BE FINISHED! SOME CASES ARE STILL LEFT UNCHECKED.
        int dim = PlayerTable.getMatrixDimension();
        if(x + 1 < dim && y + 1 < dim && x - 1 > 0 && y - 1 > 0){ //to dodge ArrayIndexOutOfBoundsException
            if(localBoard[x-1][y-1] != null)
                return 3;
            if(localBoard[x-1][y+1] != null)
                return 2;
            if(localBoard[x+1][y-1] != null)
                return 1;
            if(localBoard[x+1][y+1] != null)
                return 0;
        }
        return 0;
    }

    public int[] getCardCoords(int x, int y, PlaceableCard[][] localBoard){
        return switch (getCorner(x, y, localBoard)) {
            case 0 -> new int[]{x + 1, y + 1};
            case 1 -> new int[]{x + 1, y - 1};
            case 2 -> new int[]{x - 1, y + 1};
            case 3 -> new int[]{x - 1, y - 1};
            default -> null;
        };
    }

    public void updateDecksAndBoard(int position){
        ResourceCard choice = null;
        if(position==0||position==1||position==2||position==3) {
            choice = cardsOnTable[position];
            cardsOnTable[position] = null;
        }
        if(position == 4) {
            choice = resourceCardOnTop;
            choice.switchFrontBack();
        }
        if(position == 5) {
            choice = goldCardOnTop;
            choice.switchFrontBack();
        }

        if(choice != null)
            for(int i = 0; i<3; i++)
                if(localPlayerTable.getCardsOnHand()[i] == null)
                    localPlayerTable.getCardsOnHand()[i] = choice;

        if(position == 0 || position == 1) {
            cardsOnTable[position] = resourceCardOnTop;
            cardsOnTable[position].switchFrontBack();
        }
        if(position == 2 || position == 3) {
            cardsOnTable[position] = goldCardOnTop;
            cardsOnTable[position].switchFrontBack();
        }
    }

    public ArrayList<Integer[]> getOccupiedPositions(PlaceableCard[][] localBoard){
        ArrayList<Integer[]> occupiedPositions = new ArrayList<>();
        for(int i = 0; i < localBoard.length; i++){
            for(int j = 0; j < localBoard[i].length; j++){
                if(localBoard[i][j] != null){
                    Integer[] coordinates = {i,j};
                    occupiedPositions.add(coordinates);
                }
            }
        }
        return occupiedPositions;
    }

    public void setCardSide(int card, boolean side){
        ResourceCard r = this.localPlayerTable.getCardsOnHand()[card];
        r.setFront(side);
        this.localPlayerTable.setCardOnHand(card,r);
    }
}
