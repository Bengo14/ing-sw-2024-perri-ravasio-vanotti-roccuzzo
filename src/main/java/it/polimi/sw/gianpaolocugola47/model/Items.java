package it.polimi.sw.gianpaolocugola47.model;
/**
 * This enumeration lists the items.
 * It includes the name, the image path and the symbol (used for printing symbols in the CLI) of each item.
 */
public enum Items {
    QUILL("quill","", 'q'),
    INKWELL("inkwell","",'i'),
    MANUSCRIPT("manuscript","",'m');
    private final String name;
    private final String imgPath;
    private final char symbol;

    Items(String name, String imgPath, char symbol) {
        this.name = name;
        this.imgPath = imgPath;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public char getSymbol() {
        return symbol;
    }
}
