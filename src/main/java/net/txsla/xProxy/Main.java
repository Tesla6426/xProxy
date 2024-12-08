package net.txsla.xProxy;

import net.txsla.xProxy.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args){

        // add config for these two
        int port = 25599;
        Server.password = "P@ssword";
        ServerSocket serverSocket;

        System.out.println("Starting xProxy...");
        try {
            serverSocket = new ServerSocket(port);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch  (Exception e) { e.printStackTrace();}
    }
}