package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents pattern-oriented objectives, i.e. objectives that give points only if a certain card
 * pattern is present on a given player table.
 */
public class DiagonalPatternObjective extends Objectives {

    private final boolean isAscending;
    private final Resources resource;

    /**
     * Corner constructor.
     * @param points       the points given by the card.
     * @param imgPathFront the reference to the front image's path.
     * @param imgPathBack  the reference to the back image's path.
     */
    public DiagonalPatternObjective(int points, String imgPathFront, String imgPathBack, boolean isAscending, Resources mainResource) {
        super(points, imgPathFront, imgPathBack);
        this.isAscending = isAscending;
        this.resource = mainResource;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        /*todo*/
        return 0;
    }
}
