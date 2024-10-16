package net.txsla.xProxy;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static String sep = "¦";
    private String clientName;
    public ClientHandler(Socket socket) {
        try {
            // Establish socket
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream()));
            this.clientName = bufferedReader.readLine();
            clientHandlers.add(this);

            // get name of server
            broadcast("Server " + clientName + " has connected");


        } catch (Exception e) {
            closeCurrentSocket(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String recieved;
        while (socket.isConnected()) {
            try {
                recieved = bufferedReader.readLine();

                // process incoming packet
                String name = recieved.substring(0, recieved.indexOf(sep) );
                String command = recieved.substring(recieved.indexOf(sep)+1, recieved.lastIndexOf(sep));
                String data = recieved.substring(recieved.lastIndexOf(sep)+1, recieved.length());

                System.out.println("\nincomingName : " + name);
                System.out.println("incomingCommand : " + command );
                System.out.println("incomingData : " + data+ "\n");

                broadcast(recieved);
            }catch (Exception e) {
                closeCurrentSocket(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcast(String sendData) {
        // send message to console
        System.out.println(sendData);

        // cycle through all connected xProxy clients
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // check to make sure you aren't sending the message back to yourself
                if (!clientHandler.clientName.equals(clientName)) {
                    // send message to client
                    clientHandler.bufferedWriter.write(sendData);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (Exception e) {
                closeCurrentSocket(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler() {
        // Remove client from list
        clientHandlers.remove(this);
        broadcast("SERVER " + clientName + " has disconnected");
    }
    public void closeCurrentSocket(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            // close readers & socket
            if (bufferedReader != null ) bufferedReader.close();
            if (bufferedWriter != null ) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
