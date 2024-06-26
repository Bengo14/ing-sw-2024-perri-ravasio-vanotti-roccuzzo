package it.polimi.sw.gianpaolocugola47.view.cli;

import it.polimi.sw.gianpaolocugola47.model.*;

import java.util.ArrayList;

/**
 * This class is the controller of the CLI view. It is used to manage the data that the view needs to display during the game.
 * The data gets updated by the controller of the model in the server, and then the view can access it through this controller.
 */
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

    /**
     * This method initializes the view with the data that the view needs to display.
     * @param globalObjectives: shared objectives of the match.
     * @param cardsOnHand: cards that the player has in his hand.
     * @param cardsOnTable: cards that are on the table and can be drawn.
     * @param nicknames: nicknames of the players in the match.
     */
    public void initView(Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable, String[] nicknames){
        this.objectives = globalObjectives;
        this.cardsOnTable = cardsOnTable;
        this.localPlayerTable.setCardsOnHand(cardsOnHand);
        this.nicknames = nicknames;
    }

    /**
     * This method updates the decks stored locally.
     * @param resourceCardOnTop: resource card on top of the resource card deck.
     * @param goldCardOnTop: gold card on top of the gold card deck.
     * @param drawPos: position of the board card that has been drawn.
     */
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

    /**
     * Updates point counters locally.
     * @param boardPoints: array of updated board points.
     * @param globalPoints: array of updated global points.
     */
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

    /**
     * Returns coordinates of the available positions on the board.
     * @param playablePositions: matrix of booleans that represent the availability of the positions on the board.
     * @return : an ArrayList of Integer arrays, each containing the coordinates of an available position.
     */
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

    /**
     * Checks if the position is valid.
     * @param x: x coordinate of the position, representing the row.
     * @param y: y coordinate of the position, representing the column.
     * @param playablePositions: matrix of booleans that represent the availability of the positions on the board.
     * @return : true if the position is valid, false otherwise.
     */
    public boolean checkIfValidPosition(int x, int y, boolean[][] playablePositions){
        for(Integer[] coordinates : this.getAvailablePositions(playablePositions)){
            if(coordinates[0] == x && coordinates[1] == y)
                return true;
        }
        return false;
    }

    /**
     * Returns the corner over which the card placed in position x,y will be placed
     * @param x: x coordinate of the position of the card that is yet to be placed, representing the row.
     * @param y: y coordinate of the position of the card that is yet to be placed, representing the column.
     * @param localBoard: matrix of PlaceableCard that represents the board.
     * @return : an integer representing the corner over which the card placed in position x,y will be placed.
     */
    public int getCorner(int x, int y, PlaceableCard[][] localBoard){ //0: top left, 1: top right, 2: bottom left, 3: bottom right
        int dim = PlayerTable.getMatrixDimension();
        //edge cases; board's corners
        if(x == 0 && y == 0) //top left
            return 0;
        if(x == 0 && y == dim - 1) //top right
            return 1;
        if(x == dim -1 && y == 0) //bottom left
            return 2;
        if(x == dim - 1 && y == dim - 1) //bottom right
            return 3;

        //edge cases; board's edges
        if(y == 0){ //only left-sided corners can be linked
            if(localBoard[x-1][y+1] != null)
                return 2;
            if(localBoard[x+1][y+1] != null)
                return 0;
        }
        if(y == dim-1){ //only right-sided corners can be linked
            if(localBoard[x-1][y-1] != null)
                return 3;
            if(localBoard[x+1][y-1] != null)
                return 1;
        }
        if(x == 0){ //only top-sided corners can be linked
            if(localBoard[x+1][y-1] != null)
                return 1;
            if(localBoard[x+1][y+1] != null)
                return 0;
        }
        if(x == dim-1){ //only bottom-sided corners can be linked
            if(localBoard[x-1][y-1] != null)
                return 3;
            if(localBoard[x-1][y+1] != null)
                return 2;
        }

        //common cases; board's inner cells
        if(x + 1 < dim && y + 1 < dim && x - 1 >= 0 && y - 1 >= 0){ //to dodge ArrayIndexOutOfBoundsException
            if(localBoard[x-1][y-1] != null)
                return 3;
            if(localBoard[x-1][y+1] != null)
                return 2;
            if(localBoard[x+1][y-1] != null)
                return 1;
            if(localBoard[x+1][y+1] != null)
                return 0;
        }

        //something is wrong!
        return -1;
    }

    /**
     * Returns the coordinates of the card already placed on the board that will be linked to the card yet to be placed.
     * @param x: x coordinate of the position of the card that is yet to be placed, representing the row.
     * @param y: y coordinate of the position of the card that is yet to be placed, representing the column.
     * @param localBoard: matrix of PlaceableCard that represents the board.
     * @return : coordinates of the already placed card.
     */
    public int[] getCardCoords(int x, int y, PlaceableCard[][] localBoard){
        return switch (getCorner(x, y, localBoard)) {
            case 0 -> new int[]{x + 1, y + 1};
            case 1 -> new int[]{x + 1, y - 1};
            case 2 -> new int[]{x - 1, y + 1};
            case 3 -> new int[]{x - 1, y - 1};
            default -> null;
        };
    }

    /**
     * Updates the decks and the drawable card on the table once a card has to be drawn.
     * @param position: position of the card that has been drawn.
     */
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

    /**
     * Returns coordinates of the occupied positions on the board.
     * @param localBoard: matrix of PlaceableCard that represents the board.
     * @return : an ArrayList of Integer arrays, each containing the coordinates of an occupied position.
     */
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

    /**
     * Sets the side of a card, either front ot back
     * @param card: index of the card in the player's hand.
     * @param side: true if the card is set on the front side, false if it is set on the back side.
     */
    public void setCardSide(int card, boolean side){
        ResourceCard r = this.localPlayerTable.getCardsOnHand()[card];
        r.setFront(side);
        this.localPlayerTable.setCardOnHand(card,r);
    }
}