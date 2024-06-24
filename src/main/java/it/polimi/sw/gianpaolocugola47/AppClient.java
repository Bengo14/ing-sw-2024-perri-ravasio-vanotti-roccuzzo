package it.polimi.sw.gianpaolocugola47;

import it.polimi.sw.gianpaolocugola47.network.Client;
import it.polimi.sw.gianpaolocugola47.network.VirtualServer;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketClient;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class AppClient {

    public void main() throws IOException {
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
        System.out.println("Choose the connection type: \n1 = RMI \n2 = SOCKET");
        Scanner scan = new Scanner(System.in);
        String command = scan.next();

        while (!command.equals("1") && !command.equals("2")) {
            System.out.print("Invalid input, try again: ");
            command = scan.next();
        }
        if(command.equals("1"))
            RMIClient.connectToServer();
        else if(command.equals("2")){
            SocketClient.connectToServer();
        }
    }
}

