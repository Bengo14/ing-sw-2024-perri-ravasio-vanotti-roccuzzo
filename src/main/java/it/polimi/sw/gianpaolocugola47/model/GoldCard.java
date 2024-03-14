package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;

public class GoldCard extends ResourceCard {
    private ArrayList<Resources> resourcesRequired;
    private Items itemRequired;
    private boolean pointsForCorners;

    public GoldCard(boolean isFront, String backImgPath, String frontImgPath, Resources resourceCentreBack, Corner[] corners, int points, int line, int column, ArrayList<Resources> resourcesRequired, Items itemRequired, boolean pointsForCorners) {
        super(isFront, backImgPath, frontImgPath, resourceCentreBack, corners, points, line, column);
        this.resourcesRequired = resourcesRequired;
        this.itemRequired = itemRequired;
        this.pointsForCorners = pointsForCorners;
    }

    public ArrayList<Resources> getResourcesRequired() {
        return resourcesRequired;
    }

    public void setResourcesRequired(ArrayList<Resources> resourcesRequired) {
        this.resourcesRequired = resourcesRequired;
    }

    public Items getItemRequired() {
        return itemRequired;
    }

    public void setItemRequired(Items itemRequired) {
        this.itemRequired = itemRequired;
    }

    public boolean isPointsForCorners() {
        return pointsForCorners;
    }

    public void setPointsForCorners(boolean pointsForCorners) {
        this.pointsForCorners = pointsForCorners;
    }
}
