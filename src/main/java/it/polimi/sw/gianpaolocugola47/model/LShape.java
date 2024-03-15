package it.polimi.sw.gianpaolocugola47.model;

public abstract class LShape extends Pattern {
    private Resources secondaryResource;

    public LShape(Resources mainResource, Resources secondaryResource) {
        super(mainResource);
        this.secondaryResource = secondaryResource;
    }

    public Resources getSecondaryResource() {
        return secondaryResource;
    }
}
