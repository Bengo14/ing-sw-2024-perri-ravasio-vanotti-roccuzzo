package it.polimi.sw.gianpaolocugola47.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.messages.*;
import it.polimi.sw.gianpaolocugola47.utils.GameListener;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;


public class Lobby implements GameListener {


    private final Controller controller;

    private Set<ClientSkeleton> clients = new HashSet<>();
    private int playerNumber;
    private String lobbyName;
    private boolean nickPhase;

    private boolean inGame = false;
    private boolean ended = false;
    private Server server;

    //verifica se la lobby è in una partita
    public boolean isInGame() {
        return inGame;
    }



    public Lobby(Server server) throws FileNotFoundException {
        this.controller = new Controller();
        this.server=server;
        this.nickPhase=true;
    }


    //verifica se la lobby è in una partita che è finita
    public boolean isEnded() {
        return ended;
    }


    //aggiunge un client alla lobby
    public void addClient (ClientSkeleton client){
        this.clients.add(client);
        client.addLobby(this);
    }


   //aggiorna la lobby in base ai messaggi ricevuti dal client
    public void updateFromClientSK(Message m, ClientSkeleton client){
        switch (m.getMessageType()) {

          //case


        }
    }


    /**
     * This method dispatch the messages received from the observed Game and eventually updates the lobby status.
     *
     * @param m the message received from the current Game.
     */
    public void update(Message m) throws RemoteException {
       /*TODO: implementare l'aggiornamento della lobby*/
    }


    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }



    public String getLobbyName() {
        return lobbyName;
    }



    public int getPlayerNumber() {
        return playerNumber;
    }



    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }



    public void setGame(String nickname) {
        /*TODO: implementare la creazione del gioco*/
    }


    //metodo per ottenere i client della lobby
    public Set<ClientSkeleton> getClients() {

        return this.clients;
    }
    public Controller getController() {
        return controller;
    }



    //metodo per rimuovere un client dalla lobby
    public void removeClient(ClientSkeleton client) {
        this.clients.remove(client);
    }


    public void disconnect(ClientSkeleton clientD) {
       /*TODO: implementare la disconnessione del client*/

    }

}

