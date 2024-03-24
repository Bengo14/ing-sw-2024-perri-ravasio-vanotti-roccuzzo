package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents resource-oriented objectives, i.e. objectives that give points only if three occurrences
 * of a particular resource are present on a given player table.
 */
public class ResourceObjective extends Objectives{

    private final Resources mainResource;
    /**
     * Corner constructor.
     * @param resource      the resource needed to receive points.
     * @param imgPathFront  the reference to the front image's path.
     * @param imgPathBack   the reference to the back image's path.
     */
    public ResourceObjective(String imgPathFront, String imgPathBack, Resources resource) {
        super(2, imgPathFront, imgPathBack); //nb le carte obiettivo danno SEMPRE 2 punti indipendentemente dalla risorsa
        this.mainResource = resource;
    }

    public Resources getResource() {
        return mainResource;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        /*todo*/
        return 0; //sempre 2 punti per 2 risorse
    }
}
