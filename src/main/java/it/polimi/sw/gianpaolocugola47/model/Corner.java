package it.polimi.sw.gianpaolocugola47.model;

import java.io.Serializable;

/**
 * This class represents a corner of a card.
 * It contains the information of the corner's status, such as if the corner is covered ot not, if the corner can be
 * covered by another card or if any resource/item is present. Information on the corner covering a particular instance
 * of a given corner is also present.
 */
public class Corner implements Serializable {
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
    /**
     * Check if the corner is buildable.
     * @return true if the corner is buildable, false otherwise.
     */
    public boolean isBuildable() {
        return isBuildable;
    }
    /**
     * Check if the corner is a resource.
     * @return true if the corner is a resource, false otherwise.
     */
    public boolean isResource() {
        return isResource;
    }
    /**
     * Check if the corner is covered.
     * @return true if the corner is covered, false otherwise.
     */
    public boolean isCovered() {
        return isCovered;
    }
    /**
     * Check if the corner is an item.
     * @return true if the corner is an item, false otherwise.
     */
    public boolean isItem(){return isItem;}
    /**
     * Set the corner as covered.
     */
    public void setIsCovered(){
        this.isCovered = true;
    }
    /**
     * get the corner linked to this corner.
     */
    public Corner getLinkedCorner() {
        return this.linkedCorner;
    }
    /**
     * set the corner linked to this corner.
     */
    public void setLinkedCorner(Corner corner) {
        this.linkedCorner = corner;
    }
    /**
     * get the resource of this corner.
     */
    public Resources getResource(){
        return this.resource;
    }
    /**
     * get the item of this corner.
     */
    public Items getItem(){
        return this.item;
    }
    /**
     * get the type of the corner.
     */
    public String getCornerType(){
        if(this.item != null)
            return Character.toString(this.item.getSymbol());
        else if(this.resource != null)
            return "%s%s\033[0m".formatted(this.resource.getAsciiEscape(), this.resource.getSymbol());
        else if(isBuildable)
            return "+";
        else if(isCovered)
            return "c";
        else
            return " ";
    }
}



