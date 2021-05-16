package app.server;

import app.shared.request.Request;
import app.shared.request.RequestType;
import app.shared.response.Response;
import app.shared.response.ResponseType;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final Server server;
    private final AtomicLong checks;
    private final String target;
    private String solution = "";

    public ServerThread(Socket socket, Server server, AtomicLong checks) {
        this.socket = socket;
        this.server = server;
        this.checks = checks;
        this.target = server.getTarget();
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String cleartext = "";
            boolean solved = false;

            while (true) {
                boolean shouldBreak = false;
                Request request = Request.receive(in);
                Response response = new Response();

                if (server.getSolved().get()){
                    response.setResponseType(ResponseType.STOP);
                    response.setData("hash found, value="+cleartext);
                    response.send(out);
                    break;
                }

                if (request.getRequestType().equals(RequestType.REQUEST_JOB)){
                    response.setResponseType(ResponseType.SEND_JOB);
                    response.setData(server.getJobsQueue().take().serialize());
                }
                if (request.getRequestType().equals(RequestType.SEND_VALUE)){
                    this.checks.incrementAndGet();
                    String[] parts = request.getData().split(",");
                    if (target.equals(parts[0])){
                        response.setResponseType(ResponseType.STOP);
                        shouldBreak = true;
                        this.server.getSolved().set(true);
                        solved = true;
                        cleartext = parts[1];
                    }
                }
                if (solved){
                    response.setResponseType(ResponseType.STOP);
                    response.setData("hash found, value="+cleartext);
                    shouldBreak = true;
                }
                response.send(out);
                if (solved){
                    this.server.solveHash(cleartext);
                }
                if (shouldBreak){
                    break;
                }
            }
            System.out.println("Client: " + socket.getInetAddress().getHostAddress() + " disconnected");
            socket.close();
            in.close();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}