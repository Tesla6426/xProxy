package net.txsla.xProxy.external_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
Initialize Method

public static void initialiseXProxy() {
        String password =
        String ip =
        int port =

        System.out.println("[ProxyChat] starting xProxy client");
        new Thread(() -> {
            Socket socket;
            try {
                socket = new Socket(ip, port);
                xProxyClient client = new xProxyClient(socket, proxyName, password);
                client.listener();
                client.send();
                xProxyClient.out = ("con¦" + password);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


*/





// use this to connect to xProxy using your own programs/plugins
// this is unused in the server files and can be removed to save on storage or ram if necessary
public class xProxyClient {
    public final String xProxyClientVersion = "0.1";
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String clientName, password;
    public static String in, out;
    public static String sep = "¦";


    public xProxyClient(Socket socket, String clientName, String password) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ));
            this.bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream()));
            this.clientName = clientName;
            this.password = password;
        }catch (Exception e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }
    public void send() {
        try {
            // establish connection
            bufferedWriter.write( clientName + "¦con¦" + password);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            while (socket.isConnected()) {
                if (out != null) {
                    bufferedWriter.write( clientName + "¦" + out);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    out = null;
                }
            }
        }
        catch (Exception e) {
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }
    public void listener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String recievedData;
                System.out.println("[xProxy] Listening thread started");
                while (socket.isConnected() ) {
                    try {
                        recievedData = bufferedReader.readLine();

                        // make your own method to handle the data
                        // YourCode.YourMethod(recievedData);

                    } catch (Exception e) {
                        closeClient(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            // close readers & socket
            System.out.println("[xProxy] disconnected - closing client");
            if (bufferedReader != null ) bufferedReader.close();
            if (bufferedWriter != null ) bufferedWriter.close();
            if (socket != null) socket.close();

            System.out.println("[xProxy] attempting to reconnect..."); // or not lol, remind me to finish this code later

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void reconnect() {
        while (socket.isClosed()) {


        }
    }
}
