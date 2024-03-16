package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represent a corner of a card.
 * It contains the information of the corner's status:the possibility of building card or if is already build on it,the empty and covered state,
 * the resources or the items on it.
 */
public class Corner {
    private final boolean isBuildable;
    private final boolean isEmpty;
    private boolean isCovered;
    private Corner linkedCorner;
    private final Resources resource;
    private final Items item;

    /**
     * Corner constructor.
     *
     * @param isBuildable boolean of the possibility to build on it.
     * @param isEmpty boolean that represent the empty state of it.
     * @param item the item on the corner.
     * @param resource the resource on the corner.
     */
    public Corner(boolean isBuildable, boolean isEmpty, Resources resource, Items item) {
        this.isBuildable = isBuildable;
        this.isEmpty = isEmpty;
        this.isCovered = false; //default
        this.linkedCorner = null;
        this.resource = resource;
        this.item = item;
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
    public Resources getResource() {
        return resource;
    }
    public Items getItem() {
        return item;
    }
}
