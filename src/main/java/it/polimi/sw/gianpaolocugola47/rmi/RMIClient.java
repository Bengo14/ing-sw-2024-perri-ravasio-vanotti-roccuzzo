package it.polimi.sw.gianpaolocugola47.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient extends UnicastRemoteObject implements VirtualView {

    private VirtualServer server;

    public RMIClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException{
        this.server.connect(this);
        System.err.println("Client connected");
        runCli();
    }
    private void runCli() throws RemoteException {
        System.out.println("Welcome to Codex Naturalis!");
        /*todo*/
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();
            if (command == 0) {
                server.reset();
            }
        }
    }

    public static void main(String[] args){

        /*todo scelta tecnologia di rete da usare*/
        try {
            Registry registry = LocateRegistry.getRegistry(RMIServer.SERVER_ADDRESS, RMIServer.SERVER_PORT);
            VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
            new RMIClient(server).run();
        } catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
