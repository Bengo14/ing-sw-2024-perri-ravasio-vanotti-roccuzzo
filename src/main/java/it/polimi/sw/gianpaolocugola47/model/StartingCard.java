package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents all the starting cards.
 */
public class StartingCard extends PlaceableCard {
    private ArrayList<Resources> resourcesCentreBack;

    /**
     * Corner constructor.
     * @param backImgPath refer to the image of the back.
     * @param frontImgPath refer to the image of the front.
     */
    public StartingCard(String backImgPath, String frontImgPath) {
        super(backImgPath, frontImgPath);
        setCoordinates(PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos());
    }

    public ArrayList<Resources> getResourcesCentreBack() {
        return resourcesCentreBack;
    }

    public void setResourcesCentreBack(ArrayList<Resources> resourcesCentreBack) {
        this.resourcesCentreBack = resourcesCentreBack;
    }
    @Override
    public void updateResourceCounter(int[] counter){
        if(isFront()){
            for(int i=0; i<4; i++)
                counter[i]++;
        }
        else{
            for(int i=4; i<8; i++){
                if(getCorner(i).isResource()){
                   Corner corner = getCorner(i);
                   counter[corner.getResource().ordinal()]++;
                }
            }
            for(Resources resource : resourcesCentreBack){
                counter[resource.ordinal()]++;
            }
        }
    }

}
