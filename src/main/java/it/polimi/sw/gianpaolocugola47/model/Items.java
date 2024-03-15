package it.polimi.sw.gianpaolocugola47.model;

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
    public String getName() {
        return name;
    }
    public String getImgPath() {
        return imgPath;
    }
}
