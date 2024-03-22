package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTableTest {
    @Test
    public void testConstructor() {
        MainTable mainTable = new MainTable();
        assertNotNull(mainTable);
        assertFalse(mainTable.isEndGame());
    }

    @Test
    public void setEndGame() {
        MainTable mainTable = new MainTable();
        mainTable.setEndGame();
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