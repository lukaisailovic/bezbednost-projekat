package app.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 2021;

    public Server() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {

            Socket socket = serverSocket.accept();

            ServerThread server_thread = new ServerThread(socket, this);

            Thread thread = new Thread(server_thread);

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
