package it.polimi.sw.gianpaolocugola47.socket;

import it.polimi.sw.gianpaolocugola47.controller.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SocketClientHandler implements VirtualSocketView {
    private final Controller controller;
    private final SocketServer socketServer;
    private final BufferedReader input;
    private final VirtualSocketView view;

    public SocketClientHandler(Controller controller, SocketServer socketServer, BufferedReader input, BufferedWriter output) {
        this.controller = controller;
        this.socketServer = socketServer;
        this.input = input;
        this.view = new SocketClientProxy(output);
    }

    public void runVirtualView() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                case "connect" -> {}
                case "reset" -> {}
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    /*@Override
    public void showValue(Integer number) {
        synchronized (this.view) {
            this.view.showValue(number);
        }
    }*/

}

