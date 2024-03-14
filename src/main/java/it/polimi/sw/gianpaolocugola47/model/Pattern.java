package it.polimi.sw.gianpaolocugola47.model;

public abstract class Pattern {
    private Resources mainResource;

    public Pattern(Resources mainResource) {
        this.mainResource = mainResource;
    }

    public Resources getMainResource() {
        return mainResource;
    }

    public void setMainResource(Resources mainResource) {
        this.mainResource = mainResource;
    }

    public abstract void checkPattern(Corner corner);
}
