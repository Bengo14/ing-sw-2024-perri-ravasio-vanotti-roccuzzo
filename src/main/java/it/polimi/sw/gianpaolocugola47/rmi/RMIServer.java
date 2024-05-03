package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;
import it.polimi.sw.gianpaolocugola47.model.StartingCard;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements VirtualServer {

    public static final int SERVER_PORT = 1234;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private static RMIServer server;
    private final Controller controller;
    private final List<VirtualView> clients;
    private int numOfPlayers = -1;
    private volatile boolean terminated = false;

    public static RMIServer getServer() {
        if (RMIServer.server == null) {
            try {
                RMIServer.server = new RMIServer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RMIServer.server;
    }

    private RMIServer() throws RemoteException {
        super(0);
        this.controller = new Controller();
        this.clients = new ArrayList<>();
        pingStart();
    }

    @Override
    public int connect(VirtualView client) throws RemoteException {
        synchronized (this.clients) {
            if(this.clients.size() == this.numOfPlayers || (numOfPlayers==-1 && !this.clients.isEmpty())) {
                System.err.println("Connection Refused");
                return -1;
            }
            else {
                System.out.println("New client connected");
                this.terminated = false;
                this.clients.add(client);
                return this.clients.indexOf(client);
            }
        }
    }
    private void pingStart() {
       new Thread(()->{
           while(true) {
               while (terminated)
                   Thread.onSpinWait();
               //System.err.println("Ping control");
               VirtualView view = null;
               try {
                   synchronized (this.clients) {
                       for (VirtualView v : this.clients) {
                           view = v;
                           v.ping();
                       }
                   }
               } catch (RemoteException e) {
                   synchronized (this.clients) {
                       try {
                           terminateGame(false, clients.indexOf(view));
                       } catch (RemoteException ex) {
                           e.printStackTrace();
                       }
                   }
               }
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }
    private void terminateGame(boolean gameOver, int clientId) throws RemoteException {
        System.err.println("terminating the game...");
        this.terminated = true;

        synchronized (this.clients) {
            if(gameOver) {
                for (int i=0; i<clients.size(); i++)
                    if (i != clientId)
                        clients.get(i).gameOver();
            } else {
                for (int i = 0; i < clients.size(); i++)
                    if (i != clientId)
                        clients.get(i).terminate();
            }
        }
        resetGame();
    }
    private void resetGame() {
        synchronized (this.controller) {
            controller.resetGame();
        }
        this.numOfPlayers = -1;
        synchronized (this.clients) {
            clients.clear();
        }
    }
    @Override
    public void setNumOfPlayers(int num) throws RemoteException {
        synchronized (this.controller) {
            this.controller.setNumOfPlayers(num);
        }
        this.numOfPlayers = num;
    }
    @Override
    public void addPlayer(int id, String nickname) throws RemoteException {
        synchronized (this.controller) {
            this.controller.addPlayer(id, nickname);
            System.out.println("Player "+nickname+" added with id "+id);
            if (this.controller.getNumOfPlayersCurrentlyAdded() == numOfPlayers) {
                System.out.println("Game started");
                startGame();
            }
        }
    }
    private void startGame() throws RemoteException {
        synchronized (this.clients) {
            for(VirtualView client : this.clients) {
                client.startGame();
            }
        }
        this.controller.drawStartingCards();
    }

    public void startingCardDrawn(StartingCard card, int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(playerId).startingCardDrawn(card);
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void setStartingCard(int playerId, StartingCard card) throws RemoteException{
        this.controller.setStartingCardAndDrawObjectives(playerId, card);
    }
    public void secretObjectivesDrawn(Objectives[] obj, int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(playerId).secretObjectivesDrawn(obj);
                } catch (RemoteException ignored) {}
            }
        }
    }
    @Override
    public void setSecretObjective(int playerId, Objectives obj) throws RemoteException{
        this.controller.setSecretObjectiveAndUpdateView(playerId, obj);
    }

    public void setViewFixedParams(PlayerTable[] tables, Objectives[] globalObj) {
        String[] nicknames = new String[numOfPlayers];
        for(int i = 0; i<numOfPlayers; i++)
            nicknames[i] = tables[i].getNickName();

        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : clients) {
                        int i = clients.indexOf(view);
                        view.setViewFixedParams(nicknames, globalObj, tables[i].getSecretObjective());
                    }
                } catch (RemoteException ignored) {}
            }
        }
    }
    public void updateView(PlayerTable[] tables, int[] boardPoints, int[] globalPoints, ResourceCard[] cardsOnTable) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    for(VirtualView view : clients) {
                        int i = clients.indexOf(view);
                        view.updateView(tables[i].getCardsOnHand(), tables[i].getPlacedCards());
                        view.updateView(boardPoints, globalPoints, cardsOnTable);
                    }
                } catch (RemoteException ignored) {}
            }
        }
    }
    public void updateView(PlayerTable table, int[] boardPoints, int[] globalPoints, ResourceCard[] cardsOnTable) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(table.getId()).updateView(table.getCardsOnHand(), table.getPlacedCards());
                    for(VirtualView view : clients)
                        view.updateView(boardPoints, globalPoints, cardsOnTable);
                } catch (RemoteException ignored) {}
            }
        }
    }
    public void showTurn(int playerId) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(playerId).showTurn();
                } catch (RemoteException ignored) {}
            }
        }
    }
    public void showPlayablePositions(int playerId, boolean[][] matrix) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(playerId).showPlayablePositions(matrix);
                } catch (RemoteException ignored) {}
            }
        }
    }
    public void showWinner(int winner) {
        synchronized (this.clients) {
            if (!this.clients.isEmpty()) {
                try {
                    this.clients.get(winner).showWinner();
                    terminateGame(true, winner);
                } catch (RemoteException ignored) {}
            }
        }
    }

    public void login() throws RemoteException {

    }

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received public message: " + message.getMessage());
        for (VirtualView client : this.clients) {
            client.receiveMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received private message: " + message.getMessage());
        for (VirtualView client : this.clients) {
            if(client.getNickname().equals(message.getReceiver())){
                client.receivePrivateMessage(message);
            }
        }
    }

    @Override
    public boolean isNicknameAvailable(String nickname, int id) throws RemoteException {
        for(VirtualView client: this.clients)
            if(client.getNickname().equals(nickname) && clients.indexOf(client) != id)
                return false;
        return true;
    }

    @Override
    public String[] getNicknames() throws RemoteException {
        String[] nicknames = new String[this.clients.size()];
        for(int i = 0; i<this.clients.size(); i++)
            nicknames[i] = this.clients.get(i).getNickname();
        return nicknames;
    }

    public static void main(String[] args) {

        String name = "VirtualServer";
        System.setProperty("java.rmi.server.hostname", SERVER_ADDRESS);
        try {
            RMIServer.server = new RMIServer();
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            registry.rebind(name, RMIServer.server);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        System.err.println("Server ready ---> IP: "+SERVER_ADDRESS+", Port: "+SERVER_PORT);
    }
}
