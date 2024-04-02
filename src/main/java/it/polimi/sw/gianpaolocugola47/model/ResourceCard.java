package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents all the resource cards.
 *
 */
public class ResourceCard extends PlaceableCard {
    private final Resources resourceCentreBack;
    private final int points;

    /**
     * Corner constructor.
     *
     * @param backImgPath refers to the image in the back.
     * @param frontImgPath refers to the image in the front.
     * @param resourceCentreBack resource in the back side.
     */
    public ResourceCard(String backImgPath, String frontImgPath, int points, Resources resourceCentreBack) {
        super(backImgPath, frontImgPath);
        this.points = points;
        this.resourceCentreBack = resourceCentreBack;
    }

    public Resources getResourceCentreBack() {
        return resourceCentreBack;
    }

    public int getPoints(PlayerTable playerTable){
        return this.points;
        //nothing else needed
    }
    @Override
    public void updateResourceCounter(int[] counter){
        if(isFront()){
            for(int i=0; i<4; i++){
                Corner corner = getCorner(i);
                if(corner.isResource()){
                    counter[corner.getResource().ordinal()]++;
                } else if (corner.isItem()) {
                    counter[corner.getItem().ordinal() + 4]++;
                }
            }
        }
        else{
            counter[resourceCentreBack.ordinal()]++;
        }
    }
}
