package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents the player's board, it contains all the card of the player(starting card, played card, secret objective card),
 * the nickName, the id, the first token(if he's first),
 * the point on the board, all the point included the secret objective and the resource counter.
 *
 */
public class PlayerTable {
    private int id;
    private String nickName;
    private Colours playerColour;
    private boolean isFirst;
    private int objectivePoints;
    private int globalObjectivePoints;
    private int[] resourceCounter;
    private Objectives secretObjective;
    private StartingCard startingCard;
    private ResourceCard[] cardsOnHand;

    /**
     * Class PlayerTable constructor.
     *
     * @param id the id of the player.
     *
     * @param nickName the nickname chose by the player for a game.
     *
     * @param isFirst a boolean that represent the first player.
     *
     * @param secretObjective the player's secret objectives.
     *
     * @param startingCard the first card of the player.
     *
     * @param cardsOnHand the player's hand.
     */
    public PlayerTable(int id, Colours playerColour, String nickName, boolean isFirst, Objectives secretObjective, StartingCard startingCard, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.playerColour = playerColour;
        this.nickName = nickName;
        this.isFirst = isFirst;
        this.objectivePoints = 0;
        this.globalObjectivePoints = 0;
        this.resourceCounter = new int[]{0,0,0,0};
        this.secretObjective = secretObjective;
        this.startingCard = startingCard;
        this.cardsOnHand = cardsOnHand;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst(boolean first) {
        isFirst = first;
    }
    public int getObjectivePoints() {
        return objectivePoints;
    }
    private void addObjectivePoints(int objectivePoints) {
        this.objectivePoints += objectivePoints;
    }
    public int getGlobalObjectivePoints() {
        return globalObjectivePoints;
    }
    private void addGlobalObjectivePoints(int globalObjectivePoints) {
        this.globalObjectivePoints += globalObjectivePoints;
    }
    public int getResourceCounter(int position) {
        return resourceCounter[position];
    }
    public void addResourceCounter(int position, int resource) {
        this.resourceCounter[position] += resource;
    }
    public Objectives getSecretObjective() {
        return secretObjective;
    }
    public StartingCard getStartingCard() {
        return startingCard;
    }
    public ResourceCard getCardOnHand(int position) {
        return cardsOnHand[position];
    }
    public void setCardOnHand(int position, ResourceCard card) {
        this.cardsOnHand[position] = card;
    }
}
