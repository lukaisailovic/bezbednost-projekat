package app.client;

import app.shared.request.Request;
import app.shared.request.RequestType;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int SERVER_PORT = 2021;
    private static final String SERVER_HOST = "localhost";
    private final Socket socket;

    public Client() throws Exception {
        this.socket = new Socket(SERVER_HOST, SERVER_PORT);
    }

    public void start() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        System.out.println("Connected to server on port "+SERVER_PORT);


        int i = 0;
        while (true) {

            if(i < 5){
                Request request = new Request(RequestType.REQUEST_JOB);
                request.setData("ATTEMPT "+(i+1));
                request.send(out);
            } else {
                Request request = new Request(RequestType.STOP);
                request.setData("ATTEMPT "+(i+1));
                request.send(out);
                break;
            }
            i++;

        }

        socket.close();
        System.out.println("Disconnecting");
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
