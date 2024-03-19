package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private final int MATRIX_DIMENSION = 41;
    private final int STARTING_CARD_POS = 20;
    private final int id;
    private final String nickName;
    private final Colours playerColour;
    private boolean isFirst;
    private int[] resourceCounter;
    private final Objectives secretObjective;
    private final StartingCard startingCard;
    private PlaceableCard[] cardsOnHand;
    private PlaceableCard[][] placedCards;

    /**
     * Class PlayerTable's constructor.
     *
     * @param id the id of the player.
     * @param nickName the nickname chose by the player for a game.
     * @param isFirst a boolean that represent the first player.
     * @param secretObjective the player's secret objectives.
     * @param startingCard the first card of the player.
     * @param cardsOnHand the player's hand.
     */
    public PlayerTable(int id, Colours playerColour, String nickName, boolean isFirst, Objectives secretObjective, StartingCard startingCard, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.playerColour = playerColour;
        this.nickName = nickName;
        this.isFirst = isFirst;
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
    public void setCardOnHandInTheEmptyPosition(PlaceableCard card) {
        int position = 0;
        /*todo*/
        this.cardsOnHand[position] = card;
    }

    /**
     *This method checks if a card can be placed on another one and in case places it.
     * @param onHandCard Card on player's hand, to be placed.
     * @param onTableCardX X coordinate of on table card.
     * @param onTableCardY Y coordinate of on table card.
     * @param onTableCardCorner corner's id from 0 to 3 (independently of front/back).
     * @return true if the card has been placed.
     */
    public boolean checkAndPlaceCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner) {
        boolean isPlaceable = false;
        /*todo*/
        if(isPlaceable){
            placeCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
        }
        return isPlaceable;
    }
    private void placeCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner){
        /*todo*/
    }
    public int getGlobalObjectivePoints(Objectives[] objectives){
        /*todo*/
        return 0;
    }
    private int getSecretObjectivePoints(){
        /*todo*/
        return 0;
    }

}


