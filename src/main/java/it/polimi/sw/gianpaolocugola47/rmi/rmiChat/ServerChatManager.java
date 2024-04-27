package it.polimi.sw.gianpaolocugola47.rmi.rmiChat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerChatManager extends UnicastRemoteObject implements ChatServer {
    private final ArrayList<ChatClient> clients;
    public static final int SERVER_PORT = 4321;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private int numOfPlayers = -1;
    private volatile boolean terminated = false;

    protected ServerChatManager() throws RemoteException {
        this.clients = new ArrayList<>();
    }

    @Override
    public int login(ChatClient client) throws RemoteException {
        synchronized (this.clients) {
            if(this.clients.size() == this.numOfPlayers || (numOfPlayers==-1 && !this.clients.isEmpty())) {
                System.err.println("Connection to chat Refused");
                return -1;
            }
            else {
                System.out.println("New client connected to chat");
                this.terminated = false;
                this.clients.add(client);
                return this.clients.indexOf(client);
            }
        }
    }

    @Override
    public void logout(ChatClient client) throws RemoteException {
        this.clients.remove(client);
    }

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received public message: " + message.getMessage());
        for (ChatClient client : this.clients) {
            client.receiveMessage(message);
        }
    }

    @Override
    public void sendPrivateMessage(ChatMessage message) throws RemoteException {
        System.out.println("Server received private message: " + message.getMessage());
        for (ChatClient client : this.clients) {
            client.receiveMessage(message);
        }
    }

    private void startServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
        try{
            registry.bind("RMIChat",this);
        } catch(Exception e){
            e.printStackTrace();
        }
        System.err.println("Server ready ---> IP: " + SERVER_ADDRESS +", Port: " + SERVER_PORT);
    }

    public static void main(String[] args){
        try{
            new ServerChatManager().startServer();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
