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

    /**
     * This method returns the points given to the player after placing a specific resource card on the board.
     * @param playerTable : the player table of the player that placed the card.
     * @return : the number of points obtained.
     */
    public int getPoints(PlayerTable playerTable){
        if(this.getIsFront())
            return this.points;
        else
            return 0;
        // nothing else needed
    }

    /**
     * This method returns the points attribute.
     * If the card is placed on the back, it gives no points; otherwise, it returns the number of
     * points specified by the respective attribute.
     * @return : the number of points obtained.
     */
    public int getThisPoints(){
        if(this.getIsFront())
            return this.points;
        else
            return 0;
    }

    /**
     * This method updates the resource counter after a card is placed.
     * @param counter : the counter to update.
     */
    @Override
    public void updateResourceCounter(int[] counter) {
        if(getIsFront()){
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

    /**
     * This method returns the resourceCentreBack attribute formatted using ASCII escape codes.
     * Used for printing resource cards in the CLI.
     * @return : the resourceCentreBack attribute formatted using ASCII escape codes.
     */
    public String resourceCentreBackToString(){
        return "%s%s\u001B[0m".formatted(resourceCentreBack.getAsciiEscape(), resourceCentreBack.getSymbol());
    }

    public String toString(){
        return "ResourceCard: resource: "+resourceCentreBack.toString();
    }
}
