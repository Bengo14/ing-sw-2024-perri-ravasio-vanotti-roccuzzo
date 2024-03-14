package it.polimi.sw.gianpaolocugola47.model;

public class Corner {
    private boolean isBuildable;
    private boolean isEmpty;
    private boolean isCovered;
    private Corner overlappingCorner;
    private Resources resourceType;
    private Items itemType;

    public Corner(boolean isBuildable, boolean isEmpty, boolean isCovered, Corner overlappingCorner, Resources resourceType, Items itemType) {
        this.isBuildable = isBuildable;
        this.isEmpty = isEmpty;
        this.isCovered = isCovered;
        this.overlappingCorner = overlappingCorner;
        this.resourceType = resourceType;
        this.itemType = itemType;
    }

    public boolean isBuildable() {
        return isBuildable;
    }

    public void setBuildable(boolean buildable) {
        isBuildable = buildable;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        isCovered = covered;
    }

    public Corner getOverlappingCorner() {
        return overlappingCorner;
    }

    public void setOverlappingCorner(Corner overlappingCorner) {
        this.overlappingCorner = overlappingCorner;
    }

    public Resources getResourceType() {
        return resourceType;
    }

    public void setResourceType(Resources resourceType) {
        this.resourceType = resourceType;
    }

    public Items getItemType() {
        return itemType;
    }

    public void setItemType(Items itemType) {
        this.itemType = itemType;
    }
}
