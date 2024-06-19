package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.annotations.Expose;

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
    public static final int MATRIX_DIMENSION = 22; //should be 141 (max n^ of cards on table by a player is 70)
    @Expose
    public static final int STARTING_CARD_POS = 10; // should be 70
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
        this.canPlay = true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = cardsOnHand;
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
    }

    public PlayerTable(){} //for JSON parsing purposes

    public PlayerTable(int id){
        this.id = id;
        this.nickName = "";
        this.canPlay = true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = new ResourceCard[3];
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
    }

    public static int getMatrixDimension() {
        return MATRIX_DIMENSION;
    }

    public static int getStartingCardPos() {return STARTING_CARD_POS;}

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getNickName() {
        return nickName;
    }

    public boolean getCanPlay(){return canPlay;}

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public void setResourceCounter(int[] resourceCounter) {
        this.resourceCounter = resourceCounter;
    }

    public int[] getResourceCounter() {return resourceCounter;}

    protected int getResourceCounter(int position) {
        return resourceCounter[position];
    }

    public Objectives getSecretObjective() {
        return secretObjective;
    }

    public void setSecretObjective(Objectives objective){
        this.secretObjective = objective;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }

    public void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;
        this.placedCards[STARTING_CARD_POS][STARTING_CARD_POS] = this.startingCard;
        this.startingCard.setCoordinates(STARTING_CARD_POS, STARTING_CARD_POS);
        startingCard.updateResourceCounter(this.resourceCounter);
    }

    public ResourceCard[] getCardsOnHand(){return cardsOnHand;}

    public ResourceCard getCardOnHand(int position) {
        return cardsOnHand[position];
    }

    public PlaceableCard[][] getPlacedCards(){
        return placedCards;
    }

    public PlaceableCard getPlacedCard(int x, int y) {
        return this.placedCards[x][y];
    }

    public void setPlacedCards(PlaceableCard[][] placedCards) {
        this.placedCards = placedCards;
    }

    public void setCardOnHandInTheEmptyPosition(ResourceCard card) {
        for(int i=0; i<3; i++){
            if(this.cardsOnHand[i] == null) {
                this.cardsOnHand[i] = card;
                return;
            }
        }
    }

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
        ResourceCard card = cardsOnHand[onHandCard];
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
    public int setXCoordinate(int x, int corner){
        if(corner==0||corner==1)
            return x-1;
        if(corner==2||corner==3)
            return x+1;
        else
            return -1; // incorrect input: corner!=0,1,2,3
    }
    public int setYCoordinate(int y, int corner){
        if(corner==0||corner==2)
            return y-1;
        if(corner==1||corner==3)
            return y+1;
        else
            return -1; // incorrect input: corner!=0,1,2,3
    }
    public boolean isPlaceable(int x, int y) {
        if(this.placedCards[x][y] == null && hasAtLeastOneSurroundingCard(x,y)){
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

            if(placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)] != null)
                return true; // a surrounding card is not null
        }
        return false; // all surrounding cards are null
    }
    private boolean checkCorner(int x, int y, int corner){
        // this method checks only ONE corner, to check all corners it is to be called 4 times
        x=setXCoordinate(x, corner);
        y=setYCoordinate(y, corner);
        if(this.placedCards[x][y]!=null){
            if(corner==0){
                if(x>=1 && y>=1) {
                    return this.placedCards[x][y].getVisibleCorners()[3 - corner].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==1){
                if(x>=1 && y<=getMatrixDimension()-2) {
                    return this.placedCards[x][y].getVisibleCorners()[3 - corner].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==2){
                if(x<=getMatrixDimension()-2 && y>=1) {
                    return this.placedCards[x][y].getVisibleCorners()[3 - corner].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            if(corner==3){
                if(x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2) {
                    return this.placedCards[x][y].getVisibleCorners()[3 - corner].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[3 - corner].isCovered();
                } else {
                    return true; // card is on matrix's edge
                }
            }
            return false; // corner!=0,1,2,3
        }else
            return true; // card is null
    }
    private boolean isCheap(GoldCard card) {
        int[] resCounter = new int[Resources.values().length];
        System.arraycopy(this.resourceCounter, 0, resCounter, 0, Resources.values().length); // resCounter only of Resources
        for(Resources res : card.getResourcesRequired()) {
            resCounter[res.ordinal()]--; // decrease required resource
        }
        sort(resCounter);
        return resCounter[0] >= 0; // true: I have enough resources
    }
    private void placeCard(int x, int y, ResourceCard card) {
        this.placedCards[x][y]=card;
        card.setCoordinates(x,y);
        card.updateResourceCounter(this.resourceCounter);
        for (int corner = 0; corner < 4; corner++)
            linkCards(x, y, corner);
    }
    private void linkCards(int x,int y, int corner) {
        // mirror-links ONE corner for 2 cards, to link 4 corners it is to be called 4 times
        if(this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
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
    private void linkCard(int x, int y, int corner){
        // links corner AND decrease ResourceCounter[]
        this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getVisibleCorners()[3-corner].setIsCovered();
        decreaseResourceCounter(this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getVisibleCorners()[3-corner]);
        this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getVisibleCorners()[3-corner].setLinkedCorner(this.placedCards[x][y].getVisibleCorners()[corner]);
        this.placedCards[x][y].getVisibleCorners()[corner].setLinkedCorner(this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getVisibleCorners()[3-corner]);
    }
    private void decreaseResourceCounter(Corner corner) {
        if(corner.isResource()){
            this.resourceCounter[corner.getResource().ordinal()]--;
        } else if (corner.isItem()) {
            this.resourceCounter[corner.getItem().ordinal() + 4]--;
        }
    }

    public int getObjectivePoints(Objectives[] objectives){
        int points = getSecretObjectivePoints();
        for (Objectives objective : objectives) {
            points += objective.checkPatternAndComputePoints(this);
        }
        return points;
    }
    private int getSecretObjectivePoints(){
        return this.secretObjective.checkPatternAndComputePoints(this);
    }

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
    private boolean checkIfCanPlayOnCard(int x, int y){
        if (this.placedCards[x][y]!=null){
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

    public void setNickname(String nicknameLocal) { this.nickName = nicknameLocal; }

    public void setCardsOnHand(ResourceCard[] cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    public void setCardOnHand(int position, ResourceCard cardOnHand){
        this.cardsOnHand[position] = cardOnHand;
    }

    public int[][] getCardIdMatrix(){
        int[][] cardIdMatrix = new int[MATRIX_DIMENSION][MATRIX_DIMENSION];
        for(int i=0; i<MATRIX_DIMENSION; i++){
            for(int j=0; j<MATRIX_DIMENSION; j++){
                if(placedCards[i][j] != null)
                    cardIdMatrix[i][j] = placedCards[i][j].getId();
                else
                    cardIdMatrix[i][j] = -1;
            }
        }
        return cardIdMatrix;
    }

    public boolean[][] getCardSideMatrix() {
        boolean[][] cardSideMatrix = new boolean[MATRIX_DIMENSION][MATRIX_DIMENSION];
        for(int i=0; i<MATRIX_DIMENSION; i++){
            for(int j=0; j<MATRIX_DIMENSION; j++){
                if(placedCards[i][j] != null) //once a card is placed, it can be both true or false
                    cardSideMatrix[i][j] = placedCards[i][j].getIsFront();
                else //if a given position has no card, it is set to false. Since I already know where there are no cards placed, this is going to be ignored when rebuilding the card matrix
                    cardSideMatrix[i][j] = false;
            }
        }
        return cardSideMatrix;
    }

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

    public void setCardIdMatrix(int[][] cardIdMatrix) {
        this.cardIdMatrix = cardIdMatrix;
    }

    public void setCardSideMatrix(boolean[][] cardSideMatrix) {
        this.cardSideMatrix = cardSideMatrix;
    }

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
}