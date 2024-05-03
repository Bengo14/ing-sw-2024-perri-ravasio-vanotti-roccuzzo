package it.polimi.sw.gianpaolocugola47.utils;

import java.io.Serializable;

public class InfoLobby implements Serializable {

    private final String name;
    private final int player;
    private final int playerMax;
    private boolean inGame;
    private boolean ended;




    //controllo se la lobby è in gioco
    public boolean isInGame() {
        return inGame;
    }


    //controllo se la lobby è conclusa
    public boolean isEnded() {
        return ended;
    }



    public InfoLobby(String name, int n , int m, boolean started, boolean ended){
        this.name=name;
        this.playerMax=m;
        this.player=n;
        this.inGame=started;
        this.ended=ended;
    }


    //nome della lobby
    public String getName() {
        return name;
    }


    //numero di giocatori in lobby
    public int getPlayer() {
        return player;
    }


    //massimo numero di giocatori
    public int getPlayerMax() {
        return playerMax;
    }
}

