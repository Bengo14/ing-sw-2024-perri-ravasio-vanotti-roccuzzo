package it.polimi.sw.gianpaolocugola47.socket;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketClient implements VirtualSocketView {
    final BufferedReader input;
    final SocketServerProxy server;

    protected SocketClient(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new SocketServerProxy(output);
    }

    public void run() throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    private void runVirtualServer() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                /*todo*/
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli() throws RemoteException {
        //ricopiare client rmi con interfaccia socket
    }

    public static void main(String[] args) {
//
        int port= 8080;
        String host = "localhost";

        try {
            Socket serverSocket = new Socket(host, port);
            InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

            new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
        } catch (IOException e) {
            System.err.println("Error: Unable to connect to server: " + e.getMessage());
            System.exit(1);
        }
    }
}
