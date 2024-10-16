package net.txsla.xProxy;

import net.txsla.xProxy.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {

        // add config for these two
        int port = 25599;
        Server.password = "P@ssword";


        System.out.println("Starting xProxy...");
        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}