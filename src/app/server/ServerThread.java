package app.server;

import app.shared.request.Request;
import app.shared.request.RequestType;
import app.shared.response.Response;
import app.shared.response.ResponseType;


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
                boolean shouldBreak = false;
                Request request = Request.receive(in);
                System.out.println(request);
                if (request.getRequestType().equals(RequestType.REQUEST_JOB)){

                }
                if (request.getRequestType().equals(RequestType.STOP)){
                    shouldBreak = true;
                }
                if (shouldBreak){
                    break;
                }
                Response response = new Response();
                response.setResponseType(ResponseType.ACK);
                response.setData("ack");
                response.send(out);

            }
            System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " disconnected");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}