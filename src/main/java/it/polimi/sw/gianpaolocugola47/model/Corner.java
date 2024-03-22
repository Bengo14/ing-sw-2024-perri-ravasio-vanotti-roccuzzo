package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents a corner of a card.
 * It contains the information of the corner's status, such as if the corner is covered ot not, if the corner can be
 * covered by another card or if any resource/item is present. Information on the corner covering a particular instance
 * of a given corner is also present.
 */
public class Corner {
    private final boolean isBuildable;
    private final boolean isEmpty; //nb forse in questo caso avrebbe senso inizializzarlo a true o togliere definitivamente il parametro che ora è superfluo
    private boolean isCovered;
    private Corner linkedCorner;

    /**
     * Corner constructor.
     * @param isBuildable boolean of the possibility to build on it.
     * @param isEmpty boolean that represent the empty state of it.
     */
    public Corner(boolean isBuildable, boolean isEmpty) {
        this.isBuildable = isBuildable;
        this.isEmpty = isEmpty;
        this.isCovered = false; //default
        this.linkedCorner = null;
    }

    public boolean isBuildable() {
        return isBuildable;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
    public boolean isCovered() {
        return isCovered;
    }
    public void setIsCovered(){
        this.isCovered = true;
    }
    public Corner getLinkedCorner() {
        return this.linkedCorner;
    }
    public void setLinkedCorner(Corner corner) {
        this.linkedCorner=corner;
    }
}



