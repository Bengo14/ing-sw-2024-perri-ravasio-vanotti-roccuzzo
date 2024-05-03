package it.polimi.sw.gianpaolocugola47.model;
public class AdderModel {
    Integer state = 0;


    public boolean add(Integer number) {
        this.state += number;
        return true;
    }

    public Integer get() {
        return this.state;
    }
}
