package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents item-oriented objectives, i.e. objectives that give points only if a certain combination of
 * items is present on a given player table.
 */
public class ItemObjective extends Objectives{

    private final ArrayList<Items> itemsRequired;

    /**
     * ItemObjective constructor.
     *
     * @param itemsRequired ArrayList containing the items (up to three) needed to receive points.
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
        int itemOccurrences=0;
        //looking for a PAIR of the same items
        if(itemsRequired.size()<2){
            itemOccurrences=playerTable.getResourceCounter(itemsRequired.getFirst().ordinal()+4)/2;
        }else{
        //looking for a COMPLETE SET of all different items
            for(int i = 0; i< itemsRequired.size(); i++){
                int[] array = new int[itemsRequired.size()];
                array[i]=playerTable.getResourceCounter(i+4);
                Arrays.sort(array);
                itemOccurrences=array[0];
            }
        }
        return this.getPoints()*itemOccurrences;
    }
}
