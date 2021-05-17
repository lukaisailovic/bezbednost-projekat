package app.client;

import app.server.Benchmark;
import app.shared.Job;
import app.shared.request.Request;
import app.shared.request.RequestType;
import app.shared.response.Response;
import app.shared.response.ResponseType;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
    private static final int SERVER_PORT = 2021;
    private static final String SERVER_HOST = "localhost";
    private static final int MAX_WORKERS = 3;
    private final Socket socket;
    private final BlockingQueue<String> sendingQueue = new LinkedBlockingQueue<>();
    private final ThreadPoolExecutor workers = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_WORKERS);
    private final AtomicLong hashrate = new AtomicLong(0);
    private final AtomicLong lastStoredHashrate = new AtomicLong(0);
    private final ScheduledExecutorService benchmarkExecutor = Executors.newScheduledThreadPool(1);
    private long lastSent = System.currentTimeMillis();


    public Client() throws Exception {
        this.socket = new Socket(SERVER_HOST, SERVER_PORT);
    }

    public void start() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        System.out.println("Connected to server on port "+SERVER_PORT);
        Benchmark benchmark = new Benchmark(hashrate, lastStoredHashrate);
        benchmarkExecutor.scheduleAtFixedRate(benchmark,0,1,TimeUnit.SECONDS);


        while (true) {
            boolean shouldBreak = false;
            Request request = new Request();

            try {
                // if there is a value to be sent, send it;
                String preparedValue = sendingQueue.poll(200, TimeUnit.MILLISECONDS);
                request.setRequestType(RequestType.SEND_VALUE);
                if (preparedValue == null){
                    throw new Exception("Invalid string");
                }
                request.setData(preparedValue);
            } catch (Exception e) {
                // check if should request for job or report hashrate
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSent > 1000){
                    request.setRequestType(RequestType.REPORT_HASHRATE);
                    request.setData(String.valueOf(this.lastStoredHashrate.get()));
                    this.lastSent = currentTime;
                } else {
                    if (this.workers.getActiveCount() < MAX_WORKERS){
                        request.setRequestType(RequestType.REQUEST_JOB);
                        request.setData("req");
                    }
                }

            }
            request.send(out);
            Response response = Response.receive(in);
            //System.out.println(response);
            if (response.getResponseType().equals(ResponseType.SEND_JOB)){
                Job job = Job.deserialize(response.getData());
                JobConsumer jobConsumer = new JobConsumer(sendingQueue,job, hashrate);
                workers.submit(jobConsumer);
            }
            if (response.getResponseType().equals(ResponseType.STOP)){
                shouldBreak = true;
                System.out.println("Server sent STOP response.");
                System.out.println("Message: " + response.getData());
            }
            if (shouldBreak){
                break;
            }
        }

        socket.close();
        this.workers.shutdownNow();
        this.benchmarkExecutor.shutdownNow();
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
