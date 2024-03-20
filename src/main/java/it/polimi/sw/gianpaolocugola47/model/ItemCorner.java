package it.polimi.sw.gianpaolocugola47.model;

/**
 * This class represents an item corner. It contains information on the item present on a given corner.
 */
public class ItemCorner extends Corner{
    private final Items item;
    /**
     * Corner constructor.
     * @param item the item present on a given corner
     */
    public ItemCorner(Items item) {
        super(true, true);
        this.item = item;
    }

    public Items getItem() {
        return item;
    }
}
