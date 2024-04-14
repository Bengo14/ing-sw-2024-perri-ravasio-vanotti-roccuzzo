package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private static final int MATRIX_DIMENSION = 145;
    private static final int STARTING_CARD_POS = 72;
    private final int id;
    private final String nickName;
    private boolean isFirst;
    private boolean canPlay;
    private int[] resourceCounter;
    private final Objectives secretObjective;
    private final StartingCard startingCard;
    private ResourceCard[] cardsOnHand;
    private PlaceableCard[][] placedCards;

    /**
     * Class PlayerTable's constructor.
     *
     * @param id the id of the player.
     * @param nickName the nickname chose by the player for a game.
     * @param secretObjective the player's secret objectives.
     * @param startingCard the first card of the player.
     * @param cardsOnHand the player's hand.
     */
    public PlayerTable(int id, String nickName, Objectives secretObjective, StartingCard startingCard, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.nickName = nickName;
        this.isFirst = this.id == 0;
        this.canPlay=true;  //default
        this.resourceCounter = new int[]{0,0,0,0,0,0,0};
        this.secretObjective = secretObjective;
        this.startingCard = startingCard;
        this.cardsOnHand = cardsOnHand;
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
        this.placedCards[STARTING_CARD_POS][STARTING_CARD_POS] = startingCard;
        startingCard.updateResourceCounter(resourceCounter);
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

    public boolean isStartingCard(int x, int y){
        return x == STARTING_CARD_POS && y == STARTING_CARD_POS;
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
        if(checkSurroundingCorners(setXCoordinate(onTableCardX, onTableCardCorner), setYCoordinate(onTableCardY, onTableCardCorner )))
            placeCard(onTableCardX, onTableCardY,onTableCardCorner, card);
        else
            return -1;
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
    private boolean checkSurroundingCorners(int x, int y) {
        int cornersVerified = 0;
        for(int corner=0; corner<4; corner++){
            if (checkCorner(x, y, corner))
                    cornersVerified++;
        }
        return cornersVerified == 4;
    }
    private boolean checkCorner(int x, int y, int corner){
        if(corner==0){
            if(x!=0 && y!=0 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        if(corner==1){
            if(x!=0 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                return this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isBuildable() && !this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3 - corner].isCovered();
            }else
                return true;
        }
        if(corner==2){
            if(x<=getMatrixDimension()-2 && y!=0 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
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
    private void placeCard(int x,int y, int onTableCardCorner, ResourceCard card) {
        this.placedCards[setXCoordinate(x,onTableCardCorner)][setYCoordinate(y,onTableCardCorner)]=card;
        card.updateResourceCounter(this.resourceCounter);
        for (int corner = 0; corner < 4; corner++)
            linkCards(setXCoordinate(x,onTableCardCorner), setYCoordinate(y,onTableCardCorner), corner);
    }
    private void linkCards(int x,int y, int corner){
        if(corner==0){
            if(x!=0 && y!=0 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null) {
                linkCard(x, y,corner);
            }
        }
        if(corner==1){
            if(x!=0 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null)
                linkCard(x, y,corner);
        }
        if(corner==2){
            if(x<=getMatrixDimension()-2 && y!=0 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null)
                linkCard(x, y,corner);
        }
        if(corner==3){
            if(x<=getMatrixDimension()-2 && y<=getMatrixDimension()-2 && this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)]!=null)
                linkCard(x, y,corner);
        }
    }
    private void linkCard(int x, int y, int corner){
        this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3-corner].setIsCovered();
        decreaseResourceCounter(this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3-corner]);
        this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3-corner].setLinkedCorner(this.placedCards[x][y].getCorners()[corner]);
        this.placedCards[x][y].getCorners()[corner].setLinkedCorner(this.placedCards[setXCoordinate(x,corner)][setYCoordinate(y,corner)].getCorners()[3-corner]);
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
        points += objectives[0].checkPatternAndComputePoints(this);
        points += objectives[1].checkPatternAndComputePoints(this);
        return points;
    }
    private int getSecretObjectivePoints(){
        return this.secretObjective.checkPatternAndComputePoints(this);
    }

    public void checkIfCanPlay(){
        for (int i=0; i<getMatrixDimension(); i++){
            for (int j=0; j<getMatrixDimension(); j++){
                {
                    if(i==getMatrixDimension()-1 && j==getMatrixDimension()-1) { //last card
                        unsetCanPlay();
                        return;
                    }
                    if(checkInnerCorners(i, j))
                        return;
                }
            }
        }
    }
    private boolean checkInnerCorners(int onTableCardX, int onTableCardY){
        if (this.placedCards[onTableCardX][onTableCardY]!=null){
            for (int onTableCardCorner=0; onTableCardCorner<4; onTableCardCorner++){
                if(checkCorner(setXCoordinate(onTableCardX, onTableCardCorner),setYCoordinate(onTableCardY,onTableCardCorner),onTableCardCorner))
                    return true;
            }
        }return false;
    }
}