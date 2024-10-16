package net.txsla.xProxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer() {
        try {
            System.out.println("xProxy Started!");
            System.out.println("waiting for connections");
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress() + " is connecting");
                ClientHandler clientHandler = new ClientHandler(socket);

                // multithread for different connections
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (Exception e) {
            closeSocket();
            e.printStackTrace();
        }
    }
    public void closeSocket() {
        if (serverSocket!=null) {
            try { serverSocket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
