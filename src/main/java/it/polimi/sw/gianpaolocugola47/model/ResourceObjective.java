package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents resource-oriented objectives, i.e. objectives that give points only if three occurrences
 * of a particular resource are present on a given player table.
 */
public class ResourceObjective extends Objectives{

    private final Resources resource;
    /**
     * ResourceObjective constructor.
     * @param resource      the resource needed to receive points.
     * @param imgPathFront  the reference to the front image's path.
     * @param imgPathBack   the reference to the back image's path.
     */
    public ResourceObjective(String imgPathFront, String imgPathBack, Resources resource) {
        super(2, imgPathFront, imgPathBack); //always 2 points for 3 resource occurrences
        this.resource = resource;
    }

    /**
     * Returns the resource needed to receive points.
     * @return : the resource needed to receive points.
     */
    public Resources getResource() {
        return resource;
    }

    /**
     * Checks if the player table contains the required resource three times and computes the points.
     * @param playerTable : the player table of the player.
     * @return : the points made by the player.
     */
    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        // 1 resourceSet is 3 resources
        int resourceSetCounter;
        resourceSetCounter = playerTable.getResourceCounter(this.getResource().ordinal())/3;
        return this.getPoints() * resourceSetCounter;
    }
}
