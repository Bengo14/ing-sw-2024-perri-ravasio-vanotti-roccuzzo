package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.controller.Controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements VirtualServer {

    public static final int SERVER_PORT = 1234;
    public static final String SERVER_ADDRESS = "127.0.0.1";
    private final Controller controller;
    private final List<VirtualView> clients = new ArrayList<>();

    public RMIServer(Controller controller) throws RemoteException {
        super(0);
        this.controller = controller;
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        System.err.println("New client connected");
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
    @Override
    public void reset() throws RemoteException {
        System.err.println("reset request");
        // TODO Non ideale, una soluzione migliore può fare update non bloccante dei clients
        synchronized (this.clients) {
            for(var c : this.clients) {
                /*todo*/
            }
        }
    }
    public static void main(String[] args) {

        String name = "VirtualServer";
        System.setProperty("java.rmi.server.hostname", SERVER_ADDRESS);
        try {
            VirtualServer stub = new RMIServer(new Controller());
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            registry.rebind(name, stub);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        System.err.println("Server ready ---> IP: "+SERVER_ADDRESS+", Port: "+SERVER_PORT);
    }
}
