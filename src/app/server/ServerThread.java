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


            while (true) {
                boolean shouldBreak = false;
                Request request = Request.receive(in);
                Response response = new Response();
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
                        this.server.solveHash(parts[1]);
                    }
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