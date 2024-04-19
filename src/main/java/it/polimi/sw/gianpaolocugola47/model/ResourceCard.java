package it.polimi.sw.gianpaolocugola47.model;
/**
 * This class represents all the resource cards.
 *
 */
public class ResourceCard extends PlaceableCard {
    private final Resources resourceCentreBack;
    private final int points;

    /**
     * ResourceCard constructor.
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
        return this.resourceCentreBack;
    }

    public int getPoints(PlayerTable playerTable){
        return this.points;
        // nothing else needed
    }

    public int getThisPoints(){
        return this.points;
    }

    @Override
    public void updateResourceCounter(int[] counter){
        if(isFront()){
            Corner[] visibleCorners = getVisibleCorners();
            for(int i=0; i<4; i++){
                Corner corner = visibleCorners[i];
                if(corner.isResource()){
                    counter[corner.getResource().ordinal()]++;
                } else if (corner.isItem()) {
                    counter[corner.getItem().ordinal() + 4]++;
                }
            }
        }
        else{
            counter[getResourceCentreBack().ordinal()]++;
        }
    }
}
