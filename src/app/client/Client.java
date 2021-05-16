package app.client;

import app.shared.Job;
import app.shared.request.Request;
import app.shared.request.RequestType;
import app.shared.response.Response;
import app.shared.response.ResponseType;

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
            boolean shouldBreak = false;
            Request request = new Request();
            if(i < 100){
                request.setRequestType(RequestType.REQUEST_JOB);
            } else {
               request.setRequestType(RequestType.STOP);
               shouldBreak = true;
            }
            request.setData("ATTEMPT "+(i+1));
            request.send(out);
            if (shouldBreak){
                break;
            }
            Response response = Response.receive(in);
            System.out.println(response);
            if (response.getResponseType().equals(ResponseType.SEND_JOB)){
                Job job = Job.deserialize(response.getData());
                System.out.println(job);
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
