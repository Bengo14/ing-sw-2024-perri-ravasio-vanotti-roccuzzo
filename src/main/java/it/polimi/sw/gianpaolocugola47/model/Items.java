package it.polimi.sw.gianpaolocugola47.model;
/**
 * This enumeration lists the items.
 */
public enum Items {
    QUILL("quill",""),
    INKWELL("inkwell",""),
    MANUSCRIPT("manuscript","");
    private final String name;
    private final String imgPath;

    Items(String name, String imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "Items{" +
                "name: '" + name + '\'' +
                ", imgPath: '" + imgPath + '\'' +
                '}';
    }

}
