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

    protected ServerChatManager() throws RemoteException {
        this.clients = new ArrayList<>();
    }

    @Override
    public void login(ChatClient client) throws RemoteException {
        this.clients.add(client);
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
            if(client.getNickname().equals(message.getReceiver())){
                client.receivePrivateMessage(message);
            }
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
