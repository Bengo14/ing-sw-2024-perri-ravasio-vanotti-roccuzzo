package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents a resource corner. It contains information on the resource present on a given corner.
 */
public class ResourceCorner extends Corner{
    private final Resources resource;
    /**
     * Corner constructor.
     * @param resource the resource present on a given corner.
     */
    public ResourceCorner(Resources resource) {
        super(true, false);
        this.resource = resource;
    }

    public Resources getResource() {
        return resource;
    }
}
