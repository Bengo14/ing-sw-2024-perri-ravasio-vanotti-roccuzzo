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
                retString.append("%s %s\u001B[0m".formatted(resource.getAsciiEscape(), resource.getSymbol()));
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

    public String toString(){
        return "StartingCard: resources: "+resourcesCentreBack.toString();
    }
}