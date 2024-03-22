package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTableTest {
    @Test
    public void testConstructor() {
        MainTable mainTable = MainTable.getInstance();
        assertNotNull(mainTable);
        assertFalse(mainTable.isEndGame());


    }


    @Test
    void getInstance() {
    }

    @Test
    public void setEndGame() {
        MainTable mainTable = MainTable.getInstance();
        mainTable.setEndGame(true);
        assertTrue(mainTable.isEndGame());

    }

    @Test
    public void setNumOfPlayers() {

    }


    @Test
    public void getPlayersPoints() {

    }

    @Test
    void addPlayersPoints() {
    }

    @Test
    void getGlobalPlayersPoints() {
    }

    @Test
    void addGlobalPlayersPoints() {
    }

    @Test
    public void drawCardFrom() {

    }


}