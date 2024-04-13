package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the starting cards.
 */
public class StartingCard extends PlaceableCard {
    private ArrayList<Resources> resourcesCentreBack;

    /**
     * StartingCard constructor.
     *
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     */
    public StartingCard(String backImgPath, String frontImgPath) {
        super(backImgPath, frontImgPath);
        setCoordinates(PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos());
    }

    public ArrayList<Resources> getResourcesCentreBack() {
        return this.resourcesCentreBack;
    }

    @Override
    public void updateResourceCounter(int[] counter){
        if(isFront()){
            for(int i=0; i<4; i++)
                counter[i]++;
        }
        else{
            Corner[] visibleCorners = getVisibleCorners();
            for(int i=0; i<4; i++){
                if(visibleCorners[i].isResource()){
                   counter[visibleCorners[i].getResource().ordinal()]++;
                }
            }
            for(Resources resource : getResourcesCentreBack()){
                counter[resource.ordinal()]++;
            }
        }
    }

}