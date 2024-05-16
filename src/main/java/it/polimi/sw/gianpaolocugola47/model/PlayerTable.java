package it.polimi.sw.gianpaolocugola47.model;

import static java.util.Arrays.sort;

/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private static final int MATRIX_DIMENSION = 59; // should be 141
    private static final int STARTING_CARD_POS = MATRIX_DIMENSION/2;
    private int id;
    private String nickName;
    private boolean canPlay;
    private final int[] resourceCounter;
    private Objectives secretObjective;
    private StartingCard startingCard;
    private ResourceCard[] cardsOnHand;
    private final PlaceableCard[][] placedCards;

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

    public PlayerTable(int id){
        this.id = id;
        this.nickName = "";
        this.canPlay = true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = new ResourceCard[3];
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
    }

    public void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;
        this.placedCards[STARTING_CARD_POS][STARTING_CARD_POS] = this.startingCard;
        this.startingCard.setCoordinates(STARTING_CARD_POS, STARTING_CARD_POS);
        startingCard.updateResourceCounter(this.resourceCounter);
    }

    protected void setSecretObjective(Objectives objective){
        this.secretObjective = objective;
    }

    public static int getMatrixDimension() {
        return MATRIX_DIMENSION;
    }
    public static int getStartingCardPos() {return STARTING_CARD_POS;}
    public int getId() {
        return id;
    }
    public String getNickName() {
        return nickName;
    }
    public boolean getCanPlay(){return canPlay;}
    private void unsetCanPlay(){this.canPlay = false;}
    public void setId(int id) {this.id = id;}
    protected int getResourceCounter(int position) {
        return resourceCounter[position];
    }
    protected int[] getResourceCounter() {return resourceCounter;}
    public Objectives getSecretObjective() {
        return secretObjective;
    }
    public StartingCard getStartingCard() {
        return startingCard;
    }
    public ResourceCard getCardOnHand(int position) {
        return cardsOnHand[position];
    }
    public ResourceCard[] getCardsOnHand(){return cardsOnHand;}
    public PlaceableCard getPlacedCard(int x, int y) {
        return this.placedCards[x][y];
    }
    public PlaceableCard[][] getPlacedCards(){
        return placedCards;
    }

    public void setCardOnHandInTheEmptyPosition(ResourceCard card) {
        for(int i=0; i<3; i++){
            if(this.cardsOnHand[i] == null) {
                this.cardsOnHand[i] = card;
                return;
            }
        }
    }
    public void turnCardOnHand(int cardPosition){
        this.cardsOnHand[cardPosition].switchFrontBack();
    }

    /**
     *This method checks if a card can be placed on another one and in case places it.
     * @param onHandCard Card on player's hand, to be placed.
     * @param onTableCardX X coordinate of on table card.
     * @param onTableCardY Y coordinate of on table card.
     * @param onTableCardCorner corner's id from 0 to 3 (independently of front/back).
     * @return the points made by the player if the card has been placed, else returns -1.
     */
    public int checkAndPlaceCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner) {
        int points;
        ResourceCard card = cardsOnHand[onHandCard];
        if(isPlaceable(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner))){
            if(card instanceof GoldCard && card.isFront()){
                if(isCheap((GoldCard) card))
                    placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
                else
                    return -1; // GoldCard requisites not matched: not enough Resources on the table
            }else
                placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
        }else
            return -1; // incorrect input: position is not buildable
        points = card.getPoints(this);
        cardsOnHand[onHandCard] = null; // card that will be replaced drawing
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

            x=setXCoordinate(x, corner);
            y=setYCoordinate(y, corner);
            if(placedCards[x][y] != null){
                return true; // a surrounding card is not null
            }else{
                x=setXCoordinate(x, 3-corner);
                y=setYCoordinate(y, 3-corner);
            }                
        }
        return false; // all surrounding cards are null
    }
    private boolean checkCorner(int x, int y, int corner){
        // this method checks only ONE corner, if need to check all corners it is to be called 4 times
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
    private boolean isCheap(GoldCard card){
        int[] resCounter = new int[Resources.values().length];
        System.arraycopy(this.resourceCounter, 0, resCounter, 0, Resources.values().length); // resCounter only of Resources
        for(int i=0; i<card.getResourcesRequired().toArray().length; i++){
            resCounter[card.getResourcesRequired().get(i).ordinal()]--; // decrease required resource
        }
        sort(resCounter);
        return resCounter[0] >= 0; // true: I have enough resources
        // false: I don't have enough resources
    }
    private void placeCard(int x, int y, ResourceCard card) {
        this.placedCards[x][y]=card;
        card.setCoordinates(x,y);
        card.updateResourceCounter(this.resourceCounter);
        for (int corner = 0; corner < 4; corner++)
            linkCards(x, y, corner);
    }
    private void linkCards(int x,int y, int corner) {
        // mirror-links ONE corner for 2 cards, if need to link 4 corners it is to be called 4 times
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
                        unsetCanPlay();
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

                x=setXCoordinate(x,corner);
                y=setYCoordinate(y,corner);
                if(isPlaceable(x,y))
                    return true; // found a card that is playable on this card
                else{
                    x=setXCoordinate(x,3-corner);
                    y=setYCoordinate(y,3-corner);
                }
            }
        }return false; // incorrect input :card is null
    }

    public void setNickname(String nicknameLocal) { this.nickName = nicknameLocal; }

    public void setCardsOnHand(ResourceCard[] cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }
}