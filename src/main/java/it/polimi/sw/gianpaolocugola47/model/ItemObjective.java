package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents item-oriented objectives, i.e. objectives that give points only if a certain combination of
 * items is present on a given player table.
 */
public class ItemObjective extends Objectives{

    private final ArrayList<Items> itemsRequired;

    /**
     * Corner constructor.
     * @param itemsRequired arrayList containing the items (up to three) needed to receive points.
     * @param points        the points given by the card.
     * @param imgPathFront  the reference to the front image's path.
     * @param imgPathBack   the reference to the back image's path.
     */
    public ItemObjective(int points, String imgPathFront, String imgPathBack, ArrayList<Items> itemsRequired) {
        super(points, imgPathFront, imgPathBack);
        this.itemsRequired = itemsRequired;
    }

    protected ArrayList<Items> getItemsRequired() {
        return itemsRequired;
    }

    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        /*todo*/
        return 0;
    }
}
