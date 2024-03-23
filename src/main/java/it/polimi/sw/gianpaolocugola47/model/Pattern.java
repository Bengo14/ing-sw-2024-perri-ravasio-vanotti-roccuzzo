package it.polimi.sw.gianpaolocugola47.model;

public abstract class Pattern {

    private Resources mainResource;
    private Resources secondaryResource;

    public Pattern(Resources mainResource, Resources secondaryResource) {
        this.mainResource = mainResource;
        this.secondaryResource=secondaryResource;
    }

    public Resources getMainResource() {
        return mainResource;
    }

    public Resources getSecondaryResource() {
        return secondaryResource;
    }

    public abstract int checkPattern(PlayerTable playerTable);

    /**
     * check if the ResourceOfTheCard==ResourceOfThePattern, also checks if card is flagged for objective
     * @param card card[i][j]
     * @return
     */
    boolean isMainResourceMatched(PlaceableCard card){
        if(getMainResource() .equals (((ResourceCard)card).getResourceCentreBack()) && !card.getIsFlaggedForObjective()){
            return true;
        }else{
            return false;
        }
    }
}