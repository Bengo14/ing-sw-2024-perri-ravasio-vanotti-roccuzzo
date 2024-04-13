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
        boolean isPlaceable = false;
        int points;
        ResourceCard card = cardsOnHand[onHandCard];
        if(onTableCardCorner==0 && checkCorner0(onTableCardX, onTableCardY)) isPlaceable = true;
        if(onTableCardCorner==1 && checkCorner1(onTableCardX, onTableCardY)) isPlaceable = true;
        if(onTableCardCorner==2 && checkCorner2(onTableCardX, onTableCardY)) isPlaceable = true;
        if(onTableCardCorner==3 && checkCorner3(onTableCardX, onTableCardY)) isPlaceable = true;
        if(isPlaceable)
            placeCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
        else return -1;
        points = card.getPoints(this);
        cardsOnHand[onHandCard] = null; // card that will be replaced drawing
        return points;
    }
    private void placeCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner){
        if(onTableCardCorner==0){placeOnCorner0(onHandCard, onTableCardX, onTableCardY);}
        if(onTableCardCorner==1){placeOnCorner1(onHandCard, onTableCardX, onTableCardY);}
        if(onTableCardCorner==2){placeOnCorner2(onHandCard, onTableCardX, onTableCardY);}
        if(onTableCardCorner==3){placeOnCorner3(onHandCard, onTableCardX, onTableCardY);}
    }
    private void placeOnCorner0(int onHandCard, int x, int y){
        this.placedCards[x-1][y-1] = this.cardsOnHand[onHandCard];
        this.placedCards[x][y].getCorners()[0].setIsCovered();
        this.placedCards[x][y].getCorners()[0].setLinkedCorner(this.placedCards[x-1][y-1].getCorners()[3]);
        this.placedCards[x-1][y-1].getCorners()[3].setLinkedCorner(this.placedCards[x][y].getCorners()[0]);
        this.cardsOnHand[onHandCard].updateResourceCounter(this.resourceCounter);
        decreaseResourceCounter(this.placedCards[x][y].getCorners()[0]);
        if(y>=2 && this.placedCards[x][y-2]!=null){
            this.placedCards[x][y-2].getCorners()[1].setIsCovered();
            decreaseResourceCounter(this.placedCards[x][y-2].getCorners()[1]);
            this.placedCards[x-1][y-1].getCorners()[2].setLinkedCorner(this.placedCards[x][y-2].getCorners()[1]);
            this.placedCards[x][y-2].getCorners()[1].setLinkedCorner(this.placedCards[x-1][y-1].getCorners()[2]);
        }
        if(x>=2 && this.placedCards[x-2][y]!=null){
            this.placedCards[x-2][y].getCorners()[2].setIsCovered();
            decreaseResourceCounter(this.placedCards[x][y-2].getCorners()[2]);
            this.placedCards[x-1][y-1].getCorners()[1].setLinkedCorner(this.placedCards[x][y-2].getCorners()[2]);
            this.placedCards[x][y-2].getCorners()[2].setLinkedCorner(this.placedCards[x-1][y-1].getCorners()[1]);
        }
        if(x>=2 && y>=2 && this.placedCards[x-2][y-2]!=null){
            this.placedCards[x-2][y-2].getCorners()[3].setIsCovered();
            decreaseResourceCounter(this.placedCards[x-2][y-2].getCorners()[3]);
            this.placedCards[x-1][y-1].getCorners()[0].setLinkedCorner(this.placedCards[x-2][y-2].getCorners()[3]);
            this.placedCards[x-2][y-2].getCorners()[3].setLinkedCorner(this.placedCards[x-1][y-1].getCorners()[0]);
        }
    }
    private void placeOnCorner1(int onHandCard, int x, int y){
        this.placedCards[x-1][y+1] = this.cardsOnHand[onHandCard];
        this.placedCards[x][y].getCorners()[1].setIsCovered();
        this.placedCards[x][y].getCorners()[1].setLinkedCorner(this.placedCards[x-1][y+1].getCorners()[2]);
        this.placedCards[x-1][y+1].getCorners()[2].setLinkedCorner(this.placedCards[x][y].getCorners()[1]);
        this.cardsOnHand[onHandCard].updateResourceCounter(this.resourceCounter);
        decreaseResourceCounter(this.placedCards[x][y].getCorners()[1]);
        if(y<=(MATRIX_DIMENSION-3) && this.placedCards[x][y+2]!=null){
            this.placedCards[x][y+2].getCorners()[0].setIsCovered();
            decreaseResourceCounter(this.placedCards[x][y+2].getCorners()[0]);
            this.placedCards[x-1][y+1].getCorners()[3].setLinkedCorner(this.placedCards[x][y+2].getCorners()[0]);
            this.placedCards[x][y+2].getCorners()[0].setLinkedCorner(this.placedCards[x-1][y+1].getCorners()[2]);
        }
        if(x>=2 && this.placedCards[x-2][y]!=null){
            this.placedCards[x-2][y].getCorners()[3].setIsCovered();
            decreaseResourceCounter(this.placedCards[x-2][y].getCorners()[3]);
            this.placedCards[x-1][y+1].getCorners()[0].setLinkedCorner(this.placedCards[x-2][y].getCorners()[3]);
            this.placedCards[x-2][y].getCorners()[3].setLinkedCorner(this.placedCards[x-1][y+1].getCorners()[0]);
        }
        if(x>=2 && y<=(MATRIX_DIMENSION-3) && this.placedCards[x-2][y+2]!=null){
            this.placedCards[x-2][y+2].getCorners()[2].setIsCovered();
            decreaseResourceCounter(this.placedCards[x-2][y+2].getCorners()[2]);
            this.placedCards[x-1][y+1].getCorners()[1].setLinkedCorner(this.placedCards[x-2][y+2].getCorners()[2]);
            this.placedCards[x-2][y+2].getCorners()[2].setLinkedCorner(this.placedCards[x-1][y+1].getCorners()[1]);
        }
    }
    private void placeOnCorner2(int onHandCard, int x, int y){/*todo*/}
    private void placeOnCorner3(int onHandCard, int x, int y){/*todo*/}

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

    public boolean checkIfCanPlay(){
        for (int i=0; i<MATRIX_DIMENSION; i++){
            for (int j=0; j<MATRIX_DIMENSION; j++){
                {
                    if((i==0 && j==0) || (i==getMatrixDimension()-1 && j==0)){
                        j++;
                    }
                    if(i==0 && j==getMatrixDimension()-1){
                        i++;
                        j=0;
                    }
                    if(i==getMatrixDimension()-1 && j==getMatrixDimension()-1){ //last card
                        this.canPlay=false;
                        return true;
                    }
                    //check corner 0
                    if(checkCorner0(i, j)) return true;
                    //check corner 1
                    if(checkCorner1(i, j)) return true;
                    //check corner 2
                    if(checkCorner2(i, j)) return true;
                    //check corner 3
                    if(checkCorner3(i, j)) return true;
                }
            }
        }
    return false; //if return false there is an error in the code
    }
    private boolean checkCorner0(int x, int y){
        if(x!=0 || y!=0){
            if(this.placedCards[x][y]!=null && this.placedCards[x][y].getVisibleCorners()[0].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[0].isCovered() && this.placedCards[x-1][y-1]==null){
                if(x==1 && y==1)
                    return true;
                if(x==1 && y>=2 && ((this.placedCards[x][y-2]==null) || (this.placedCards[x][y-2]!=null && this.placedCards[x][y-2].getVisibleCorners()[1].isBuildable() && !this.placedCards[x][y-2].getVisibleCorners()[1].isCovered())))
                    return true;
                if(x>=2 && y==1 && ((this.placedCards[x-2][y]==null) || (this.placedCards[x-2][y]!=null && this.placedCards[x-2][y].getVisibleCorners()[2].isBuildable() && !this.placedCards[x-2][y].getVisibleCorners()[2].isCovered())))
                    return true;
                if(x>=2 && y>=2 && ((this.placedCards[x][y-2]==null) || (this.placedCards[x][y-2]!=null && this.placedCards[x][y-2].getVisibleCorners()[1].isBuildable() && !this.placedCards[x][y-2].getVisibleCorners()[1].isCovered())) && ((this.placedCards[x-2][y-2]==null) || (this.placedCards[x-2][y-2]!=null && this.placedCards[x-2][y-2].getVisibleCorners()[3].isBuildable() && !this.placedCards[x-2][y-2].getVisibleCorners()[3].isCovered())) && ((this.placedCards[x-2][y]==null) || (this.placedCards[x-2][y]!=null && this.placedCards[x-2][y].getVisibleCorners()[2].isBuildable() && !this.placedCards[x-2][y].getVisibleCorners()[2].isCovered())))
                    return true;
            }
        }
        return false;
    }
    private boolean checkCorner1(int x, int y){
        if(x!=0 || y!=(getMatrixDimension()-1)){
            if(this.placedCards[x][y]!=null && this.placedCards[x][y].getVisibleCorners()[1].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[1].isCovered() && this.placedCards[x+1][y+1]==null){
                if(x==1 && y==(getMatrixDimension()-2))
                    return true;
                if(x==1 && y<=(getMatrixDimension()-3) && ((this.placedCards[x][y+2]==null) || (this.placedCards[x][y+2]!=null && this.placedCards[x][y+2].getVisibleCorners()[0].isBuildable() && !this.placedCards[x][y+2].getVisibleCorners()[0].isCovered()))){
                    return true;
                }
                if(x>=2 && y==(getMatrixDimension()-2) && ((this.placedCards[x-2][y]==null) || (this.placedCards[x-2][y]!=null && this.placedCards[x-2][y].getVisibleCorners()[3].isBuildable() && !this.placedCards[x-2][y].getVisibleCorners()[3].isCovered()))){
                    return true;
                }
                if(x>=2 && y<=(getMatrixDimension()-3) && ((this.placedCards[x][y+2]==null) || (this.placedCards[x][y+2]!=null && this.placedCards[x][y+2].getVisibleCorners()[0].isBuildable() && !this.placedCards[x][y+2].getVisibleCorners()[0].isCovered())) && ((this.placedCards[x-2][y+2]==null) || (this.placedCards[x-2][y+2]!=null && this.placedCards[x-2][y+2].getVisibleCorners()[2].isBuildable() && !this.placedCards[x-2][y+2].getVisibleCorners()[2].isCovered())) && ((this.placedCards[x-2][y]==null) || (this.placedCards[x-2][y]!=null && this.placedCards[x-2][y].getVisibleCorners()[3].isBuildable() && !this.placedCards[x-2][y].getVisibleCorners()[3].isCovered()))){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkCorner2(int x, int y){
        if(x!=(getMatrixDimension()-1) || y!=0){
            if(this.placedCards[x][y]!=null && this.placedCards[x][y].getVisibleCorners()[2].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[2].isCovered() && this.placedCards[x+1][y-1]==null) {
                if (x == (getMatrixDimension() - 2) && y == 1)
                    return true;
                if (x == (getMatrixDimension() - 2) && y>=2 && ((this.placedCards[x][y-2]==null) || (this.placedCards[x][y-2]!=null && this.placedCards[x][y-2].getVisibleCorners()[3].isBuildable() && !this.placedCards[x][y-2].getVisibleCorners()[3].isCovered()))) {
                    return true;
                }
                if(x<=(getMatrixDimension()-3) && y==1 && ((this.placedCards[x+2][y]==null) || (this.placedCards[x+2][y]!=null && this.placedCards[x+2][y].getVisibleCorners()[0].isBuildable() && !this.placedCards[x+2][y].getVisibleCorners()[0].isCovered()))){
                    return true;
                }
                if(x<=(getMatrixDimension()-3) && y>=2 && ((this.placedCards[x][y-2]==null) || (this.placedCards[x][y-2]!=null && this.placedCards[x][y-2].getVisibleCorners()[3].isBuildable() && !this.placedCards[x][y-2].getVisibleCorners()[3].isCovered())) && ((this.placedCards[x+2][y-2]==null) || (this.placedCards[x+2][y-2]!=null && this.placedCards[x+2][y-2].getVisibleCorners()[1].isBuildable() && !this.placedCards[x+2][y-2].getVisibleCorners()[1].isCovered())) && ((this.placedCards[x+2][y]==null) || (this.placedCards[x+2][y]!=null && this.placedCards[x+2][y].getVisibleCorners()[0].isBuildable() && !this.placedCards[x+2][y].getVisibleCorners()[0].isCovered()))){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkCorner3(int x, int y){
        if (x!=(getMatrixDimension()-1) && y!=getMatrixDimension()-1) {
            if(this.placedCards[x][y]!=null && this.placedCards[x][y].getVisibleCorners()[3].isBuildable() && !this.placedCards[x][y].getVisibleCorners()[3].isCovered() && this.placedCards[x+1][y+1]==null){
                if(x==getMatrixDimension()-2 && y==getMatrixDimension()-2)
                    return true;
                if(x==(getMatrixDimension()-2) && y<=(getMatrixDimension()-3) && ((this.placedCards[x][y+2]==null) || (this.placedCards[x][y+2]!=null && this.placedCards[x][y+2].getVisibleCorners()[2].isBuildable() && !this.placedCards[x][y+2].getVisibleCorners()[2].isCovered()))){
                    return true;
                }
                if(x<=(getMatrixDimension()-3) && y==(getMatrixDimension()-2) && ((this.placedCards[x+2][y]==null) || (this.placedCards[x+2][y]!=null && this.placedCards[x+2][y].getVisibleCorners()[1].isBuildable() && !this.placedCards[x+2][y].getVisibleCorners()[1].isCovered()))){
                    return true;
                }
                if(x<=(getMatrixDimension()-3) && y<=(getMatrixDimension()-3) && ((this.placedCards[x][y+2]==null) || (this.placedCards[x][y+2]!=null && this.placedCards[x][y+2].getVisibleCorners()[2].isBuildable() && !this.placedCards[x][y+2].getVisibleCorners()[2].isCovered())) && ((this.placedCards[x+2][y+2]==null) || (this.placedCards[x+2][y+2]!=null && this.placedCards[x+2][y+2].getVisibleCorners()[0].isBuildable() && !this.placedCards[x+2][y+2].getVisibleCorners()[0].isCovered())) && ((this.placedCards[x+2][y]==null) || (this.placedCards[x+2][y]!=null && this.placedCards[x+2][y].getVisibleCorners()[1].isBuildable() && !this.placedCards[x+2][y].getVisibleCorners()[1].isCovered()))){
                    return true;
                }
            }
        }
        return false;
    }



}