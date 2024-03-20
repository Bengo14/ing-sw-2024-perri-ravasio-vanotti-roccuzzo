package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents resource-oriented objectives, i.e. objectives that give points only if three occurrences
 * of a particular resource are present on a given player table.
 */
public class ResourceObjective extends Objectives{
    private final Resources resource;
    /**
     * Corner constructor.
     * @param resource      the resource needed to receive points.
     * @param points        the points given by the card.
     * @param imgPathFront  the reference to the front image's path.
     * @param imgPathBack   the reference to the back image's path.
     */
    public ResourceObjective(int points, String imgPathFront, String imgPathBack, Resources resource) {
        super(points, imgPathFront, imgPathBack);
        this.resource = resource;
    }

    public Resources getResource() {
        return resource;
    }
}
