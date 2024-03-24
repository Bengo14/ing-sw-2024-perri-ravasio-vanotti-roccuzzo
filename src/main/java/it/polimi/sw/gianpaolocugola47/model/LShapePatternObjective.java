package it.polimi.sw.gianpaolocugola47.model;

public class LShapePatternObjective extends Objectives{

    private final String orientation;
    private final Resources mainResource;
    private final Resources secondaryResource;

    public LShapePatternObjective(int points, String imgPathFront, String imgPathBack, String orientation, Resources mainResource, Resources secondaryResource){
        super(points, imgPathFront, imgPathBack);
        this.orientation = orientation;
        this.mainResource = mainResource;
        this.secondaryResource = secondaryResource;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        /*todo*/
        return 0;
    }
}
