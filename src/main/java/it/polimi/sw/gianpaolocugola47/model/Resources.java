package it.polimi.sw.gianpaolocugola47.model;
/**
 * This enumeration lists the resources of a card.
 */
public enum Resources {
    ANIMAL("blue","","\033[0;34m", 'A'),
    FUNGI("red","", "\033[0;31m", 'F'),
    PLANT("green","", "\033[0;32m", 'P'),
    INSECTS("purple","", "\033[0;35m", 'I');

    private final String colour;
    private final String imgPath;
    private final String asciiEscape;
    private final char symbol;

    Resources(String colour, String imgPath, String asciiEscape, char symbol) {
        this.colour = colour;
        this.imgPath = imgPath;
        this.asciiEscape = asciiEscape;
        this.symbol = symbol;
    }

    public String getColour() {
        return colour;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getAsciiEscape() {
        return asciiEscape;
    }

    public char getSymbol() {
        return symbol;
    }
}
