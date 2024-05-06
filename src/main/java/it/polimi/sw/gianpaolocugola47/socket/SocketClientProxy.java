package it.polimi.sw.gianpaolocugola47.socket;


import java.io.BufferedWriter;
import java.io.PrintWriter;

public class SocketClientProxy implements VirtualSocketView {
    final PrintWriter output;

    public SocketClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    public void showValue(Integer number) {
        output.println("update");
        output.println(number);
        output.flush();
    }

    public void reportError(String details) {
        output.println("error");
        output.println(details);
        output.flush();
    }
}
