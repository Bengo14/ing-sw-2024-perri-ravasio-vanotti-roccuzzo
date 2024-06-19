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

    /**
     * This method returns the items required to achieve the points.
     * @return the items required to achieve the points.
     */
    public ArrayList<Items> getItemsRequired() {
        return itemsRequired;
    }

    /**
     * This method checks if the player table contains the required items and computes the points.
     * @param playerTable the player table to check.
     * @return the points obtained by the player.
     */
    @Override
    public int checkPatternAndComputePoints(PlayerTable playerTable) {
        int itemsSetCounter;
        //looking for a PAIR of the same item
        if(this.getItemsRequired().size()<=1){
            itemsSetCounter =playerTable.getResourceCounter(getItemsRequired().getFirst().ordinal()+4)/2;
        }else{
        //looking for a COMPLETE SET of all different items
            int[] array = new int[getItemsRequired().size()];
            for(int i = 0; i<getItemsRequired().size(); i++){
                array[i]=playerTable.getResourceCounter(i+4);
            }
            Arrays.sort(array);
            itemsSetCounter =array[0];
        }
        return getPoints()* itemsSetCounter;
    }

    /**
     * This method returns a string representation of the ItemObjective.
     * @return a string representation of the ItemObjective.
     */
    public String toString(){
        return "ItemObjective: "+getPoints()+" points, required items: "+getItemsRequired().toString();
    }
}