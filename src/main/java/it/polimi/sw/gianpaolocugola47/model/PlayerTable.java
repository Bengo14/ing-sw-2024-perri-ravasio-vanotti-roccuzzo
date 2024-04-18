package it.polimi.sw.gianpaolocugola47.model;

import static java.util.Arrays.sort;

/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private static final int MATRIX_DIMENSION = 61; // should be 141
    private static final int STARTING_CARD_POS = MATRIX_DIMENSION/2;
    private final int id;
    private final String nickName;
    private boolean isFirst;
    private boolean canPlay;
    private int[] resourceCounter;
    private Objectives secretObjective;
    private StartingCard startingCard;
    private ResourceCard[] cardsOnHand;
    private PlaceableCard[][] placedCards;

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
        this.isFirst = this.id == 0;
        this.canPlay=true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.cardsOnHand = cardsOnHand;
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
    }

    protected void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;
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
    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst() {
        isFirst = true;
    }
    public int getResourceCounter(int position) {
        return resourceCounter[position];
    }
    public Objectives getSecretObjective() {
        return secretObjective;
    }
    public StartingCard getStartingCard() {
        return startingCard;
    }
    public PlaceableCard getCardOnHand(int position) {
        return cardsOnHand[position];
    }
    public PlaceableCard getPlacedCard(int x, int y) {
        return placedCards[x][y];
    }
    public PlaceableCard getElement(int xIndex, int yIndex){
        return this.placedCards[xIndex][yIndex];
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
            if(card instanceof GoldCard){
                if(isCheap((GoldCard) card))
                    placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
                else return -1; // not enough Resources on the table
            }else
                placeCard(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner), card);
        }else
            return -1; //not placeable here
        points = card.getPoints(this, setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner));
        cardsOnHand[onHandCard] = null; // card that will be replaced drawing
        return points;
    }
    public int setXCoordinate(int x, int corner){
        if(corner==0||corner==1)
            return x-1;
        if(corner==2||corner==3)
            return x+1;
        else
            return -1;
    }
    public int setYCoordinate(int y, int corner){
        if(corner==0||corner==2)
            return y-1;
        if(corner==1||corner==3)
            return y+1;
        else
            return -1;
    }
    private boolean isCheap(GoldCard card){
        int[] resCounter = new int[this.resourceCounter.length];
        System.arraycopy(this.resourceCounter, 0, resCounter, 0, this.resourceCounter.length);
        for(int i=0; i<card.getResourcesRequired().toArray().length; i++){
            resCounter[card.getResourcesRequired().get(i).ordinal()]--;
        }
        sort(resCounter);
        return resCounter[0] >= 0;
    }
    public boolean isPlaceable(int x, int y) {
        if(this.placedCards[x][y] == null){
            int cornersVerified = 0;
            for(int corner=0; corner<4; corner++){
                if (checkCorner(x, y, corner))
                    cornersVerified++;
            }
            return cornersVerified == 4;
        }else
            return false;
    }
    private boolean checkCorner(int x, int y, int corner){
        if(corner==0){
            if(x>=1 && y>=1 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        if(corner==1){
            if(x>=1 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        if(corner==2){
            if(x<=getMatrixDimension()-2 && y>=1 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        if(corner==3){
            if(x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        return false;
    }
    private void placeCard(int x,int y, ResourceCard card) {
        this.placedCards[x][y]=card;
        card.setCoordinates(x,y);
        card.updateResourceCounter(this.resourceCounter);
        for (int corner = 0; corner < 4; corner++)
            linkCards(x, y, corner);
    }
    private void linkCards(int x,int y, int corner){
        if(corner==0){
            if(x>=0 && y>=0 && this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)]!=null)
                linkCard(x, y, corner);
        }
        if(corner==1){
            if(x>=0 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)]!=null)
                linkCard(x, y, corner);
        }
        if(corner==2){
            if(x<=getMatrixDimension()-2 && y>=0 && this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)]!=null)
                linkCard(x, y, corner);
        }
        if(corner==3){
            if(x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)]!=null)
                linkCard(x, y, corner);
        }
    }
    private void linkCard(int x, int y, int corner){
        this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getCorners()[3-corner].setIsCovered();
        decreaseResourceCounter(this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getCorners()[3-corner]);
        this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getCorners()[3-corner].setLinkedCorner(this.placedCards[x][y].getCorners()[corner]);
        this.placedCards[x][y].getCorners()[corner].setLinkedCorner(this.placedCards[setXCoordinate(x, corner)][setYCoordinate(y, corner)].getCorners()[3-corner]);
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
            for (int i = 0; i < getMatrixDimension(); i++)
                for (int j = 0; j < getMatrixDimension(); j++)
                    this.placedCards[i][j].setFlaggedForObjective(false);
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
                        unsetCanPlay(); // last card, no playable position found
                        return;
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
                if(isPlaceable(setXCoordinate(x, corner),setYCoordinate(y,corner)))
                    return true;
            }
        }return false;
    }
}