package app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 2021;
    private final ServerSocket serverSocket;


    public Server() throws Exception {
        this.serverSocket = new ServerSocket(PORT);
    }
    public void start() throws IOException {
        System.out.println("Server waiting for connections on port "+PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, this);
            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
