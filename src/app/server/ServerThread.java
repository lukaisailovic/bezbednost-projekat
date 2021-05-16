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
                Response response = new Response();
                if (request.getRequestType().equals(RequestType.REQUEST_JOB)){
                    System.out.println(request);
                    response.setResponseType(ResponseType.SEND_JOB);
                    response.setData(server.getJobsQueue().take().serialize());
                    System.out.println("Job sent to client");
                }
                if (request.getRequestType().equals(RequestType.SEND_VALUE)){
                    System.out.println("Client sent "+ request.getData());
                }
                if (request.getRequestType().equals(RequestType.STOP)){
                    System.out.println(request);
                    shouldBreak = true;
                }
                if (shouldBreak){
                    break;
                }

                response.send(out);
            }
            System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " disconnected");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}