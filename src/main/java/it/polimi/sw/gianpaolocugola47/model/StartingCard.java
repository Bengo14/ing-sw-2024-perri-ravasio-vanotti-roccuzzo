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

    /**
     * Sets the resources of the centre back of the card.
     * @return : the resources of the centre back of the card.
     */
    public ArrayList<Resources> getResourcesCentreBack() {
        return this.resourcesCentreBack;
    }

    /**
     * This method return the resourcesCentreBack attribute in a string format using ASCII escape codes to
     * represent the respective colors. Used for printing starting cards in the CLI.
     * @return : the resourcesCentreBack attribute in a string format.
     */
    public String resourcesCentreBackToString(){
        StringBuilder retString = new StringBuilder();
        if(resourcesCentreBack.size() == 1){
            retString.append("   ");
            for(Resources resource : resourcesCentreBack){
                retString.append("%s%s\u001B[0m".formatted(resource.getAsciiEscape(), resource.getSymbol()));
            }
            retString.append("   ");
        }
        else if (resourcesCentreBack.size() == 2){
            retString.append("  ");
            for(Resources resource : resourcesCentreBack){
                retString.append("%s%s\u001B[0m".formatted(resource.getAsciiEscape(), resource.getSymbol()));
            }
            retString.append("  ");
        }
        else {
            retString.append("  ");
            for(Resources resource : resourcesCentreBack){
                retString.append("%s%s\u001B[0m".formatted(resource.getAsciiEscape(), resource.getSymbol()));
            }
            retString.append("  ");
        }
        return retString.toString();
    }

    /**
     * Updates the resource counter once the starting card is placed.
     * @param counter : the resource counter.
     */
    @Override
    public void updateResourceCounter(int[] counter) {
        if(getIsFront()) {
            for(int i=0; i<4; i++)
                counter[i]++;
        }
        else {
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

    /**
     * String representation of the starting card.
     * @return : the string representation of the starting card.
     */
    public String toString(){
        return "StartingCard: resources: "+resourcesCentreBack.toString();
    }
}