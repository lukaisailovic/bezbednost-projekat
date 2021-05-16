package app.server;

import app.shared.request.Request;
import app.shared.request.RequestType;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final Server server;

    public ServerThread(Socket socket, Server server) {

        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);


            while (true) {
                Request request = Request.receive(in);
                System.out.println(request);
                if (request.getRequestType().equals(RequestType.STOP)){
                    break;
                }

            }
            System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " disconnected");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}