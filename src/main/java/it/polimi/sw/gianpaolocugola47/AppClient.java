package it.polimi.sw.gianpaolocugola47;

import it.polimi.sw.gianpaolocugola47.network.rmi.RMIClient;
import it.polimi.sw.gianpaolocugola47.network.socket.SocketClient;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is the launcher of the client side of the application
 */
public class AppClient {
    /**
     * This method is the main client method, it asks the user to choose the connection type and then connects to the server
     * @throws IOException if the connection fails
     */
    public void main() throws IOException {

        System.out.println("Choose the connection type: \n1 = RMI \n2 = SOCKET");
        Scanner scan = new Scanner(System.in);
        String command = scan.next();

        while (!command.equals("1") && !command.equals("2")) {
            System.out.print("Invalid input, try again: ");
            command = scan.next();
        }
        if(command.equals("1"))
            RMIClient.connectToServer();
        else
            SocketClient.connectToServer();

    }
}