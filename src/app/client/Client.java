package app.client;

import app.shared.Job;
import app.shared.request.Request;
import app.shared.request.RequestType;
import app.shared.response.Response;
import app.shared.response.ResponseType;
import core.SHA1;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final int SERVER_PORT = 2021;
    private static final String SERVER_HOST = "localhost";
    private final Socket socket;
    private final BlockingQueue<String> sendingQueue = new LinkedBlockingQueue<>();

    public Client() throws Exception {
        this.socket = new Socket(SERVER_HOST, SERVER_PORT);
    }

    public void start() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        System.out.println("Connected to server on port "+SERVER_PORT);


        int i = 0;
        while (true) {
            boolean shouldBreak = false;
            Request request = new Request();

            try {
                String generatedString = sendingQueue.poll(50, TimeUnit.MILLISECONDS);
                request.setRequestType(RequestType.SEND_VALUE);
                if (generatedString == null){
                    throw new Exception("Invalid string");
                }
                String hash = SHA1.hash(generatedString);
                System.out.println("ATTEMPT: "+hash);
                request.setData(hash+","+generatedString);
            } catch (Exception e) {
                request.setRequestType(RequestType.REQUEST_JOB);
                request.setData("req");
            }
            request.send(out);
            Response response = Response.receive(in);
            System.out.println(response);
            if (response.getResponseType().equals(ResponseType.SEND_JOB)){
                Job job = Job.deserialize(response.getData());
                System.out.println(job);
                JobConsumer jobConsumer = new JobConsumer(sendingQueue,job);
                Thread thread = new Thread(jobConsumer);
                thread.start();
            }
            if (response.getResponseType().equals(ResponseType.STOP)){
                shouldBreak = true;
            }
            if (shouldBreak){
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
