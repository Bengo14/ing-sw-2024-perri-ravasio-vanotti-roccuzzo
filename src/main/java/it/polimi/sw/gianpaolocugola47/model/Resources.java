package it.polimi.sw.gianpaolocugola47.model;
/**
 * This enumeration lists the resources of a card.
 */
public enum Resources {
    ANIMAL("blue",""),
    FUNGI("red",""),
    PLANT("green",""),
    INSECTS("purple","");

    private final String colour;
    private final String imgPath;

    Resources(String colour, String imgPath) {
        this.colour = colour;
        this.imgPath = imgPath;
    }
    public String getColour() {
        return colour;
    }
    public String getImgPath() {
        return imgPath;
    }

}
