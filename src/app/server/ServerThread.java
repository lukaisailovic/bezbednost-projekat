package app.server;

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

            // client connected, send job
            out.println("POZZ");
            while (true) {

                String poruka = in.readLine();
                if (poruka.equals("exit"))
                    break;
            }
            System.out.println("Klijent sa adresom: " + socket.getInetAddress().getHostAddress() + " se diskonektovao.");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}