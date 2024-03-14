package it.polimi.sw.gianpaolocugola47.model;

public enum Items {
    Quill(),
    Inkwell(),
    Manuscript();
    private final String name;

    private Items(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
