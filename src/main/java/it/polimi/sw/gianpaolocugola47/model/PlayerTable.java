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
     * @param objectivePoints the player's board points.
     *
     * @param globalObjectivePoints all the player's points included the secret objective.
     *
     * @param resourceCounter the counter of the resources on the player's board.
     *
     * @param secretObjective the player's secret objectives.
     *
     * @param startingCard the first card of the player.
     *
     * @param cardsOnHand the player's hand.
     */
    public PlayerTable(int id, String nickName, boolean isFirst, int objectivePoints, int globalObjectivePoints, int[] resourceCounter, Objectives secretObjective, StartingCard startingCard, ResourceCard[] cardsOnHand) {
        this.id = id;
        this.nickName = nickName;
        this.isFirst = isFirst;
        this.objectivePoints = objectivePoints;
        this.globalObjectivePoints = globalObjectivePoints;
        this.resourceCounter = resourceCounter;
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

    public void setObjectivePoints(int objectivePoints) {
        this.objectivePoints = objectivePoints;
    }

    public int getGlobalObjectivePoints() {
        return globalObjectivePoints;
    }

    public void setGlobalObjectivePoints(int globalObjectivePoints) {
        this.globalObjectivePoints = globalObjectivePoints;
    }

    public int[] getResourceCounter() {
        return resourceCounter;
    }

    public void setResourceCounter(int[] resourceCounter) {
        this.resourceCounter = resourceCounter;
    }

    public Objectives getSecretObjective() {
        return secretObjective;
    }

    public void setSecretObjective(Objectives secretObjective) {
        this.secretObjective = secretObjective;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }

    public void setStartingCard(StartingCard startingCard) {
        this.startingCard = startingCard;
    }

    public ResourceCard[] getCardsOnHand() {
        return cardsOnHand;
    }

    public void setCardsOnHand(ResourceCard[] cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }
}
