package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.sort;

/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable{

    @Expose
    public static final int MATRIX_DIMENSION = 22;
    @Expose
    public static final int STARTING_CARD_POS = 10;
    @Expose
    private int id;
    @Expose
    private String nickName;
    @Expose
    private boolean canPlay;
    @Expose
    private int[] resourceCounter;
    @Expose
    private Objectives secretObjective;
    @Expose
    private StartingCard startingCard;
    @Expose
    private ResourceCard[] cardsOnHand;
    private PlaceableCard[][] placedCards;
    private int[][] cardIdMatrix = null;
    private boolean[][] cardSideMatrix = null;
    @Expose
    private ArrayList<int[]> placeOrder;

    /**
     * Class PlayerTable's constructor.
     *
     * @param id the id of the player.
     * @param nickName the nickname chose by the player for a game.
     * @param cardsOnHand the player's hand.
     */
    public PlayerTable(int id, String nickName, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.nickName = nickName;
        this.canPlay = true;
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = cardsOnHand;
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
        this.placeOrder = new ArrayList<>();
    }

    /**
     * Class PlayerTable's empty constructor.
     */
    public PlayerTable(){} //for JSON parsing purposes

    /**
     * Class PlayerTable's constructor.
     * @param id : the id of the player.
     */
    public PlayerTable(int id){
        this.id = id;
        this.nickName = "";
        this.canPlay = true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = new ResourceCard[3];
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
    }

    /**
     * This method returns the matrix dimension. The matrix is a square, so both borders are equal.
     * @return : the matrix dimension.
     */
    public static int getMatrixDimension() {
        return MATRIX_DIMENSION;
    }

    /**
     * This method returns the starting card position. It is at the centre of the board,
     * so row and column position are the same.
     * @return : the starting card position.
     */
    public static int getStartingCardPos() {return STARTING_CARD_POS;}

    /**
     * Returns the player's id.
     * @return : the id of the player.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the player's id.
     * @param id : the id of the player.
     */
    public void setId(int id) {this.id = id;}

    /**
     * Returns the player's nickname.
     * @return : the nickname of the player.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Returns whether the player can play or not.
     * @return : true if the player can play, false otherwise.
     */
    public boolean getCanPlay(){return canPlay;}

    /**
     * Sets the player's canPlay attribute.
     * @param canPlay : true if the player can play, false otherwise.
     */
    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    /**
     * Sets the player's resource counter. Used when parsing from json.
     * @param resourceCounter : the resource counter of the player.
     */
    public void setResourceCounter(int[] resourceCounter) {
        this.resourceCounter = resourceCounter;
    }

    /**
     * Returns the player's resource counter.
     * @return : the resource counter of the player.
     */
    public int[] getResourceCounter() {return resourceCounter;}

    /**
     * This method returns the resource counter of a specific resource.
     * @param position : the position of the resource in the resource counter.
     * @return : the resource counter of the player.
     */
    protected int getResourceCounter(int position) {
        return resourceCounter[position];
    }

    /**
     * This method sets the secret objective of the player.
     * @return : the player's secret objective.
     */
    public Objectives getSecretObjective() {
        return secretObjective;
    }

    /**
     * This method sets the secret objective of the player.
     * @param objective : the player's secret objective.
     */
    public void setSecretObjective(Objectives objective){
        this.secretObjective = objective;
    }

    /**
     * This method sets the starting card of the player.
     * @return : the player's starting card.
     */
    public StartingCard getStartingCard() {
        return startingCard;
    }

    /**
     * This method sets the starting card of the player and updates the resource counter.
     * @param startingCard : the player's starting card.
     */
    public void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;
        this.placedCards[STARTING_CARD_POS][STARTING_CARD_POS] = this.startingCard;
        this.startingCard.setCoordinates(STARTING_CARD_POS, STARTING_CARD_POS);
        startingCard.updateResourceCounter(this.resourceCounter);
    }

    /**
     * This method returns the cards on the player's hand.
     * @return : the cards on the player's hand.
     */
    public ResourceCard[] getCardsOnHand(){return cardsOnHand;}

    /**
     * This method returns the card on the player's hand at a specific position.
     * @param position : the position of the card on the player's hand.
     * @return : the card on the player's hand at the specified position.
     */
    public ResourceCard getCardOnHand(int position) {
        return cardsOnHand[position];
    }

    /**
     * This method returns the cards placed on the player's board.
     * @return : the cards matrix placed on the player's board.
     */
    public PlaceableCard[][] getPlacedCards(){
        return placedCards;
    }

    /**
     * This method returns the card placed on the player's board at a specific position.
     * @param x : row coordinate.
     * @param y : column coordinate.
     * @return : the card placed on the player's board at the specified position.
     */
    public PlaceableCard getPlacedCard(int x, int y) {
        if (x >= 0 && x < getMatrixDimension() && y >= 0 && y < getMatrixDimension()) {
            return placedCards[x][y];
        } else {
            return null;
        }
    }

    /**
     * This method sets a new placed card matrix, replacing the old one.
     * @param placedCards : the cards matrix placed on the player's board.
     */
    public void setPlacedCards(PlaceableCard[][] placedCards) {
        this.placedCards = placedCards;
    }

    /**
     * Checks whether the hand of a player has a null card and places a given card there.
     * @param card : the card to be placed.
     */
    public void setCardOnHandInTheEmptyPosition(ResourceCard card) {
        for(int i=0; i<3; i++){
            if(this.cardsOnHand[i] == null) {
                this.cardsOnHand[i] = card;
                return;
            }
        }
    }

    /**
     * Changes the side of the card in the specified hand position.
     * @param isFront : true if the card is front, false otherwise.
     * @param cardPosition : position of the card in the player's hand that is to be switched.
     */
    public void turnCardOnHand(int cardPosition, boolean isFront){
        this.cardsOnHand[cardPosition].setFront(isFront);
    }

    /**
     *This method checks if a card can be placed on another one and places it if that's the case.
     * @param onHandCard Card on player's hand, to be placed.
     * @param onTableCardX X coordinate of on table card.
     * @param onTableCardY Y coordinate of on table card.
     * @param onTableCardCorner corner's id from 0 to 3 (independently of front/back).
     * @return the points made by the player if the card has been placed, else returns -1.
     */
    public int checkAndPlaceCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner) {
        int points;
        ResourceCard card = getCardOnHand(onHandCard);
        if(card == null)
            return -1;
        if(isPlaceable(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner))){
            if(card instanceof GoldCard && card.getIsFront()){
                if(isCheap((GoldCard) card))
                    placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
                else
                    return -1; // GoldCard requisites not matched: not enough Resources on the table
            } else
                placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
        } else
            return -1; // incorrect input: position is not buildable
        points = card.getPoints(this);
        return points;
    }

    /**
     * This method sets the x coordinate of a card depending on the corner.
     * If a card is to be placed on the top corners of the card (0,1), this method returns
     * x - 1, since the card to be placed will be on the row above the card already on the board;
     * Corners 2 and 3 are the bottom corners, so the x coordinate is x + 1.
     * @param x : row coordinate of the card already present on the board.
     * @param corner : corner of the card already present on the board that is to be covered by the new card.
     * @return  the x coordinate of the card to be placed. -1 if the input is incorrect.
     */
    public int setXCoordinate(int x, int corner){
        if(corner==0||corner==1)
            return x-1;
        if(corner==2||corner==3)
            return x+1;
        else
            return -1; // incorrect input: corner!=0,1,2,3
    }

    /**
     * This method sets the y coordinate of a card depending on the corner.
     * If a card is to be placed on the left corners of the card (0,2), this method returns
     * y - 1, since the card to be placed will be on the column to the left of the card already on the board;
     * Corners 1 and 3 are the right corners, so the y coordinate is y + 1.
     * @param y : column coordinate of the card already present on the board.
     * @param corner : corner of the card already present on the board that is to be covered by the new card.
     * @return  the y coordinate of the card to be placed. -1 if the input is incorrect.
     */
    public int setYCoordinate(int y, int corner){
        if(corner==0||corner==2)
            return y-1;
        if(corner==1||corner==3)
            return y+1;
        else
            return -1; // incorrect input: corner!=0,1,2,3
    }

    /**
     * This method checks if a card can be placed on the table at the given coordinates.
     * @param x : row coordinate of the card to be placed.
     * @param y : column coordinate of the card to be placed.
     * @return : true if the card can be placed, false otherwise.
     */
    public boolean isPlaceable(int x, int y) {
        if(getPlacedCard(x,y) == null && hasAtLeastOneSurroundingCard(x,y)){
                int cornersVerified = 0;
                for(int corner=0; corner<4; corner++){
                    if (checkCorner(x, y, corner))
                        cornersVerified++;
                    else
                        return false; // a corner isn't verified
                }
                return cornersVerified == 4;
        }else
            return false; // card is null OR all cards around card are null
    }

    /**
     * This method checks if a card in x,y has at least one other card around.
     * @param x : row coordinate of the card.
     * @param y : column coordinate of the card.
     * @return : true if the card has at least one surrounding card, false otherwise.
     */
    private boolean hasAtLeastOneSurroundingCard(int x, int y){
        for(int corner=0; corner<4; corner++){
            if(corner==0 && (x==0 || y==0))
                corner++;
            if(corner==1 && (x==0 || y==getMatrixDimension()-1))
                corner++;
            if(corner==2 && (x==getMatrixDimension()-1 || y==0))
                corner++;
            if(corner==3 && (x==getMatrixDimension()-1 || y==getMatrixDimension()-1))
                return false; // all surrounding cards are null

            if(getPlacedCard(setXCoordinate(x, corner), setYCoordinate(y, corner)) != null)
                return true; // a surrounding card is not null
        }
        return false; // all surrounding cards are null
    }

    /**
     * This method checks if a card is on matrix's edge and if the corner is valid.
     * @param x : row coordinate of the card.
     * @param y : column coordinate of the card.
     * @param corner : corner of the card to be checked.
     * @return : true if the corner is valid or if the card is null, false otherwise.
     */
    private boolean checkCorner(int x, int y, int corner){
        // this method checks only ONE corner, to check all corners it is to be called 4 times
        x=setXCoordinate(x, corner);
        y=setYCoordinate(y, corner);
        if(getPlacedCard(x,y)!=null){
            if(corner==0){
                if(x>=1 && y>=1) {
                    return getPlacedCard(x,y).getVisibleCorners()[3 - corner].isBuildable() && !getPlacedCard(x,y).getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==1){
                if(x>=1 && y<=getMatrixDimension()-2) {
                    return getPlacedCard(x,y).getVisibleCorners()[3 - corner].isBuildable() && !getPlacedCard(x,y).getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==2){
                if(x<=getMatrixDimension()-2 && y>=1) {
                    return getPlacedCard(x,y).getVisibleCorners()[3 - corner].isBuildable() && !getPlacedCard(x,y).getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==3){
                if(x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2) {
                    return getPlacedCard(x,y).getVisibleCorners()[3 - corner].isBuildable() && !getPlacedCard(x,y).getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            return false; // corner!=0,1,2,3
        }else
            return true; // card is null
    }

    /**
     * This method checks if there are enough resources on the board to place a ResourceCard on the table.
     * Once the card is placed, the resource counter is updated.
     * @param card : the gold card to be placed.
     * @return : true if there are enough resources, false otherwise.
     */
    private boolean isCheap(GoldCard card) {
        int[] resCounter = new int[Resources.values().length];
        System.arraycopy(getResourceCounter(), 0, resCounter, 0, Resources.values().length); // resCounter only of Resources
        for(Resources res : card.getResourcesRequired()) {
            resCounter[res.ordinal()]--; // decrease required resource
        }
        sort(resCounter);
        return resCounter[0] >= 0; // true: I have enough resources
    }

    /**
     * This method places a card on the table and links it to the surrounding cards.
     * @param x : row coordinate of the card to be placed.
     * @param y : column coordinate of the card to be placed.
     * @param card : the card to be placed.
     */
    private void placeCard(int x, int y, ResourceCard card) {
        int[] coords = new int[2];
        coords[0] = x;
        coords[1] = y;
        placeOrder.add(coords);

        placedCards[x][y]=card;
        card.setCoordinates(x,y);
        card.updateResourceCounter(getResourceCounter());
        for (int corner = 0; corner < 4; corner++)
            linkCards(x, y, corner);
    }

    /**
     * This method links a card to the board card it is placed over.
     * @param x : row coordinate of the card on board.
     * @param y : column coordinate of the card on board.
     * @param corner : corner of the card on board.
     */
    private void linkCards(int x,int y, int corner) {
        // mirror-links ONE corner for 2 cards, to link 4 corners it is to be called 4 times
        if(getPlacedCard(setXCoordinate(x,corner),setYCoordinate(y,corner)) != null) {
            if(corner==0 && x>=1 && y>=1)
                linkCard(x, y, corner);
            if(corner==1 && x>=1 && y<=getMatrixDimension()-2)
                linkCard(x, y, corner);
            if(corner==2 && x<=getMatrixDimension()-2 && y>=1)
                linkCard(x, y, corner);
            if(corner==3 && x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2)
                linkCard(x, y, corner);
        }
    }

    /**
     * This method links a corner of a card to the corner of another card.
     * @param x : row coordinate of the card on the board.
     * @param y : column coordinate of the card on the board.
     * @param corner : corner of the card on the board.
     */
    private void linkCard(int x, int y, int corner){
        // links corner AND decrease ResourceCounter[]
        getPlacedCard(setXCoordinate(x,corner),setYCoordinate(y,corner)).getVisibleCorners()[3-corner].setIsCovered();
        decreaseResourceCounter(getPlacedCard(setXCoordinate(x,corner),setYCoordinate(y,corner)).getVisibleCorners()[3-corner]);
        getPlacedCard(setXCoordinate(x,corner),setYCoordinate(y,corner)).getVisibleCorners()[3-corner].setLinkedCorner(getPlacedCard(x,y).getVisibleCorners()[corner]);
        getPlacedCard(x,y).getVisibleCorners()[corner].setLinkedCorner(getPlacedCard(setXCoordinate(x,corner),setYCoordinate(y,corner)).getVisibleCorners()[3-corner]);
    }

    /**
     * This method decreases the resource counter of the player.
     * @param corner : the corner that got covered once another card is placed over it.
     */
    private void decreaseResourceCounter(Corner corner) {
        if(corner.isResource()){
            resourceCounter[corner.getResource().ordinal()]--;
        } else if (corner.isItem()) {
            resourceCounter[corner.getItem().ordinal() + 4]--;
        }
    }

    /**
     * This method returns the points made by the player with both the secret and the shared objectives.
     * @param objectives : the shared objectives of the game.
     * @return : the points made by the player.
     */
    public int getObjectivePoints(Objectives[] objectives){
        int points = getSecretObjectivePoints();
        for (Objectives objective : objectives) {
            points += objective.checkPatternAndComputePoints(this);
        }
        return points;
    }

    /**
     * This method returns the points made by the player with the secret objective.
     * @return : the points made by the player with the secret objective.
     */
    private int getSecretObjectivePoints(){
        return this.secretObjective.checkPatternAndComputePoints(this);
    }

    /**
     * Checks if the player can play any card on the board. If the board is full, the player is stuck.
     * Sets canPlay to false if the player can't play any card.
     */
    public void checkIfCanPlay(){
        for (int i=0; i<getMatrixDimension(); i++){
            for (int j=0; j<getMatrixDimension(); j++){
                {
                    if(i==getMatrixDimension()-1 && j==getMatrixDimension()-1) {
                        setCanPlay(false);
                        return; // no playable position found
                    }
                    if(checkIfCanPlayOnCard(i, j))
                        return; // playable position found
                }
            }
        }
    }

    /**
     * Checks whether the player can place a card over a specific board card or not.
     * @param x : row coordinate of the card on the board.
     * @param y : column coordinate of the card on the board.
     * @return : true if the player can place a card over the board card, false otherwise.
     */
    private boolean checkIfCanPlayOnCard(int x, int y){
        if (getPlacedCard(x,y) != null){
            for (int corner=0; corner<4; corner++){
                if(corner==0 && (x==0 || y==0))
                    corner++;
                if(corner==1 && (x==0 || y==getMatrixDimension()-1))
                    corner++;
                if(corner==2 && (x==getMatrixDimension()-1 || y==0))
                    corner++;
                if(corner==3 && (x==getMatrixDimension()-1 || y==getMatrixDimension()-1))
                    return false; // can't play on card

                if(isPlaceable(setXCoordinate(x, corner), setYCoordinate(y, corner)))
                    return true; // found a card that is playable on this card
            }
        }return false; // incorrect input :card is null
    }

    /**
     * Sets the player's nickname.
     * @param nicknameLocal : the player's nickname.
     */
    public void setNickname(String nicknameLocal) { this.nickName = nicknameLocal; }

    /**
     * Sets the player's cardsOnHand.
     * @param cardsOnHand : the cards on the player's hand.
     */
    public void setCardsOnHand(ResourceCard[] cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    /**
     * Sets a card on a player's specific hand position.
     * @param position : hand's position.
     * @param cardOnHand : the card to be set in that position.
     */
    public void setCardOnHand(int position, ResourceCard cardOnHand){
        this.cardsOnHand[position] = cardOnHand;
    }

    /**
     * Used for serializing the player table to json, since the PlacedCard matrix is not serializable.
     * Returns an int matrix with card's id are present at the coordinates of the respective cards,
     * and -1 is set where the position is null.
     * @return : the card id matrix.
     */
    public int[][] getCardIdMatrix(){
        int[][] cardIdMatrix = new int[getMatrixDimension()][getMatrixDimension()];
        for(int i=0; i<getMatrixDimension(); i++){
            for(int j=0; j<getMatrixDimension(); j++){
                if(getPlacedCard(i,j) != null)
                    cardIdMatrix[i][j] = getPlacedCard(i,j).getId();
                else
                    cardIdMatrix[i][j] = -1;
            }
        }
        return cardIdMatrix;
    }

    /**
     * Used for serializing the player table to json, since the PlacedCard matrix is not serializable.
     * Returns a boolean matrix where empty positions and back-played cards' positions are set false
     * and front-played cards' are set true.
     * @return : th
     */
    public boolean[][] getCardSideMatrix() {
        boolean[][] cardSideMatrix = new boolean[getMatrixDimension()][getMatrixDimension()];
        for(int i=0; i<getMatrixDimension(); i++){
            for(int j=0; j<getMatrixDimension(); j++){
                if(getPlacedCard(i,j) != null) //once a card is placed, it can be both true or false
                    cardSideMatrix[i][j] = getPlacedCard(i,j).getIsFront();
                else //if a given position has no card, it is set to false. Since I already know where there are no cards placed, this is going to be ignored when rebuilding the card matrix
                    cardSideMatrix[i][j] = false;
            }
        }
        return cardSideMatrix;
    }

    /**
     * This method is used to convert the id matrix and the side matrix to the card matrix.
     * If the id is set to -1 in the id matrix, null is placed at those coordinates. The card with the respective id
     * is set to the correct side and then placed and linked with the remaining cards elsewhere.
     * A resource counter parameter is used to correctly update the resource counter of the player, since
     * the placeStartingCard method overrides it and doubles the resources when loading from json.
     * @param resourceCounterParameter : the resource counter of the player, loaded from the json file.
     */
    public void idMatrixToCardMatrix(int[] resourceCounterParameter){ //to be called ONLY when parsing the id matrix from json
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
        int[] tempResCounter = new int[resourceCounterParameter.length];
        System.arraycopy(resourceCounterParameter, 0, tempResCounter, 0, tempResCounter.length);
        for(int i = 0; i < MATRIX_DIMENSION; i++){
            for(int j = 0; j < MATRIX_DIMENSION; j++){
                if(cardIdMatrix[i][j] == -1)
                    placedCards[i][j] = null;
                else {
                    if(i == STARTING_CARD_POS && j == STARTING_CARD_POS){
                        StartingCard s = (StartingCard) Deck.getCardFromGivenId(cardIdMatrix[i][j]);
                        s.setFront(cardSideMatrix[i][j]);
                        setStartingCard(s); //issue: resource counter is doubled
                    }
                    else{
                        ResourceCard c = (ResourceCard) Deck.getCardFromGivenId(cardIdMatrix[i][j]);
                        c.setFront(cardSideMatrix[i][j]);
                        placedCards[i][j] = c;
                        placedCards[i][j].setCoordinates(i, j);
                        placedCards[i][j].updateResourceCounter(this.resourceCounter);
                    }
                }
            }
        }
        for(int i = 0; i < MATRIX_DIMENSION; i++){
            for(int j = 0; j < MATRIX_DIMENSION; j++){
                if(placedCards[i][j] != null && i != STARTING_CARD_POS && j != STARTING_CARD_POS){
                    for(int corner = 0; corner < 4; corner++){
                        linkCards(i, j, corner);
                    }
                }
            }
        }
        System.arraycopy(tempResCounter, 0, this.resourceCounter, 0, resourceCounter.length);
        this.cardIdMatrix = null;
        this.cardSideMatrix = null;
    }

    /**
     * This method sets the card id matrix. Used when parsing from json.
     * @param cardIdMatrix : the card id matrix.
     */
    public void setCardIdMatrix(int[][] cardIdMatrix) {
        this.cardIdMatrix = cardIdMatrix;
    }

    /**
     * This method sets the card side matrix. Used when parsing from json.
     * @param cardSideMatrix : the card side matrix.
     */
    public void setCardSideMatrix(boolean[][] cardSideMatrix) {
        this.cardSideMatrix = cardSideMatrix;
    }

    /**
     * This method returns the player's board as a string.
     * @return : the player's board as a string.
     */
    @Override
    public String toString() {
        return "PlayerTable{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", canPlay=" + canPlay +
                ", resourceCounter=" + Arrays.toString(resourceCounter) +
                ", secretObjective=" + secretObjective +
                ", startingCard=" + startingCard +
                ", cardsOnHand=" + Arrays.toString(cardsOnHand) +
                ", placedCards=" + Arrays.toString(placedCards) +
                ", cardIdMatrix=" + Arrays.toString(cardIdMatrix) +
                ", cardSideMatrix=" + Arrays.toString(cardSideMatrix) +
                '}';
    }

    /**
     * This method returns the arraylist containing the order in which the cards were placed on the board, characterized by the coordinates of a
     * given card. The order of insertion in the arraylist is equivalent to the order of insertion in the card matrix.
     * @return : arraylist of card's coordinates from least to most recent card placed.
     */
    public ArrayList<int[]> getPlaceOrder() {
        return placeOrder;
    }

    /**
     * This method sets the arraylist containing the order in which the cards were placed on the board, characterized by the coordinates of a
     * given card. The order of insertion in the arraylist is equivalent to the order of insertion in the card matrix.
     * @param placeOrder : the arraylist of card's coordinates, from least to most recent card placed.
     */
    public void setPlaceOrder(ArrayList<int[]> placeOrder) {
        this.placeOrder = placeOrder;
    }
}