package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents pattern-oriented objectives, i.e. objectives that give points only if a certain card
 * pattern is present on a given player table.
 */
public class PatternObjective extends Objectives {
    private final Pattern pattern;
    /**
     * Corner constructor.
     * @param pattern      the pattern that needs to be matched to receive points.
     * @param points       the points given by the card.
     * @param imgPathFront the reference to the front image's path.
     * @param imgPathBack  the reference to the back image's path.
     */
    public PatternObjective(int points, String imgPathFront, String imgPathBack, Pattern pattern) {
        super(points, imgPathFront, imgPathBack);
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
