package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private static final int MATRIX_DIMENSION = 41;
    private final int STARTING_CARD_POS = 20;
    private final int id;
    private final String nickName;
    private final Colours playerColour;
    private boolean isFirst;
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
    public PlayerTable(int id, Colours playerColour, String nickName, Objectives secretObjective, StartingCard startingCard, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.playerColour = playerColour;
        this.nickName = nickName;
        this.isFirst = this.id == 0;
        this.resourceCounter = new int[]{0,0,0,0};
        this.secretObjective = secretObjective;
        this.startingCard = startingCard;
        this.cardsOnHand = cardsOnHand;
        this.placedCards = new PlaceableCard[MATRIX_DIMENSION][MATRIX_DIMENSION];
        this.placedCards[STARTING_CARD_POS][STARTING_CARD_POS] = startingCard;
    }

    public int getId() {
        return id;
    }
    public String getNickName() {
        return nickName;
    }
    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst(boolean first) {
        isFirst = first;
    }
    public int getResourceCounter(int position) {
        return resourceCounter[position];
    }
    private void addResourceCounter(int position, int resource) {
        this.resourceCounter[position] += resource;
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

    public static int getMatrixDimension() {
        return MATRIX_DIMENSION;
    }

    public int getSTARTING_CARD_POS() {
        int startingCardPos = STARTING_CARD_POS;
        return startingCardPos;
    }

    public void setCardOnHandInTheEmptyPosition(ResourceCard card) {
        for(int i=0; i<3; i++){
            if(this.cardsOnHand[i] == null) {
                this.cardsOnHand[i] = card;
                return;
            }
        }
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
        int points = 0;
        ResourceCard card = cardsOnHand[onHandCard];
        /*todo*/
        if(isPlaceable)
            placeCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
        else return -1;
        points = card.getPoints(this.placedCards);
        cardsOnHand[onHandCard] = null; // card that will be replaced drawing
        return points;
    }
    private void placeCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner){
        /*todo*/
    }
    public int getObjectivePoints(Objectives[] objectives){
        int points = getSecretObjectivePoints();
        points += objectives[0].checkPatternAndComputePoints(placedCards);
        points += objectives[1].checkPatternAndComputePoints(placedCards);
        return points;
    }
    private int getSecretObjectivePoints(){
        int points = this.secretObjective.checkPatternAndComputePoints(placedCards);
        return points;
    }
    public int getNumberOfAchievedObjectives(Objectives[] objectives){
        int counter = 0;
        int points = getSecretObjectivePoints();
        if(points > 0)
            counter++;
        points = objectives[0].checkPatternAndComputePoints(placedCards);
        if(points > 0)
            counter++;
        points = objectives[1].checkPatternAndComputePoints(placedCards);
        if(points > 0)
            counter++;
        return counter;
    }
    public PlaceableCard getElement(int xIndex, int yIndex) {
        return placedCards[xIndex][yIndex];
    }
    boolean isStartingCard(int xIndex, int yIndex){
        if(xIndex==STARTING_CARD_POS && yIndex==STARTING_CARD_POS) {
            return true;
        }else{
            return false;
        }
    }
}