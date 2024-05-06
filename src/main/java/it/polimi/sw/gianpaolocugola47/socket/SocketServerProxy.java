package it.polimi.sw.gianpaolocugola47.socket;


import java.io.BufferedWriter;
import java.io.PrintWriter;

public class SocketServerProxy implements VirtualSocketServer {
    final PrintWriter output;

    public SocketServerProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    @Override
    public void connect(VirtualSocketView client) {
        output.println("connect");
        output.flush();
    }

    @Override
    public void add(Integer number) {
        output.println("add");
        output.println(number);
        output.flush();
    }

    @Override
    public void reset() {
        output.println("reset");
        output.flush();
    }
}

