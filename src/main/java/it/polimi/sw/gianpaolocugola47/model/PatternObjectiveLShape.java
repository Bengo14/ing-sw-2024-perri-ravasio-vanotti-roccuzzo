package it.polimi.sw.gianpaolocugola47.model;

public class PatternObjectiveLShape extends Objectives{

    private String orientation;
    private Resources mainResource;
    private Resources secondaryResource;

    public PatternObjectiveLShape(int points, String imgPathFront, String imgPathBack, String orientation, Resources mainResource, Resources secondaryResource){
        super(points, imgPathFront, imgPathBack);
        this.orientation = orientation;
        this.mainResource = mainResource;
        this.secondaryResource = secondaryResource;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        return 0;
    }
}
