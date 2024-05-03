package it.polimi.sw.gianpaolocugola47.socket;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client implements VirtualView {
    final BufferedReader input;
    final ServerProxy server;

    protected Client(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new ServerProxy(output);
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
                case "update" -> this.showValue(Integer.parseInt(input.readLine()));
                case "error" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();

            if (command == 0) {
                server.reset();
            } else {
                server.add(command);
            }
        }
    }

    public void showValue(Integer number) {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.out.print("\n= " + number + "\n> ");
    }

    public void reportError(String details) {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) {
//        if (args.length < 2) {
//            System.err.println("Usage: java Client <host> <port>");
//            System.exit(1);
//        }


        int port= 8080;
        String host = "localhost";

//        try {
//            port = Integer.parseInt(args[1]);
//        } catch (NumberFormatException e) {
//            System.err.println("Error: Invalid port number.");
//            System.exit(1);
//            return;
//        }

        try {
            Socket serverSocket = new Socket(host, port);

            InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

            new Client(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
        } catch (IOException e) {
            System.err.println("Error: Unable to connect to server: " + e.getMessage());
            System.exit(1);
        }
    }
}
