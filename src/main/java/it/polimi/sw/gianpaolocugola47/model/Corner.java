package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents a corner of a card.
 * It contains the information of the corner's status, such as if the corner is covered ot not, if the corner can be
 * covered by another card or if any resource/item is present. Information on the corner covering a particular instance
 * of a given corner is also present.
 */
public class Corner {
    private final boolean isBuildable;
    private final boolean isItem;
    private final boolean isResource;
    private boolean isCovered;
    private Corner linkedCorner;
    private final Items item;
    private final Resources resource;

    /**
     * Corner constructor.
     *
     * @param isBuildable boolean of the possibility to build on it.
     * @param isItem boolean that represent the presence of an Item.
     */
    public Corner(boolean isBuildable, boolean isItem, boolean isResource, Items item, Resources resource) {
        this.isBuildable = isBuildable;
        this.isItem = isItem;
        this.isResource = isResource;
        this.isCovered = false; //default
        this.linkedCorner = null;
        this.item = item;
        this.resource = resource;
    }

    public boolean isBuildable() {
        return isBuildable;
    }
    public boolean isResource() {
        return isResource;
    }
    public boolean isCovered() {
        return isCovered;
    }
    public boolean isItem(){return isItem;}
    public void setIsCovered(){
        this.isCovered = true;
    }
    public Corner getLinkedCorner() {
        return this.linkedCorner;
    }
    public void setLinkedCorner(Corner corner) {
        this.linkedCorner = corner;
    }
    public Resources getResource(){
        return this.resource;
    }
    public Items getItem(){
        return this.item;
    }
}



