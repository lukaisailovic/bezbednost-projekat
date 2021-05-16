package app.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 2021;

    public Server() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket socket = serverSocket.accept();

            ServerThread serverThread = new ServerThread(socket, this);

            Thread thread = new Thread(serverThread);

            thread.start();
        }

    }
    public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
