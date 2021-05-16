package app.server;

import app.shared.Job;
import app.shared.Parameters;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Server {

    private static final int PORT = 2021;
    private final ServerSocket serverSocket;
    private final BlockingQueue<Job> jobsQueue = new LinkedBlockingQueue<>();
    private final AtomicLong checks = new AtomicLong(0);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public Server() throws Exception {
        this.serverSocket = new ServerSocket(PORT);
        this.populateJobQueue();
    }

    public BlockingQueue<Job> getJobsQueue() {
        return jobsQueue;
    }

    private void populateJobQueue(){
        JobScheduler jobScheduler = new JobScheduler(Parameters.MAX_PASSWORD_LENGTH);
        jobsQueue.addAll(jobScheduler.getJobs());
    }
    public void start() throws IOException {
        System.out.println("Server waiting for connections on port "+PORT);
        Benchmark benchmark = new Benchmark(checks);
        executor.scheduleAtFixedRate(benchmark,0,1,TimeUnit.SECONDS);
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, this,checks);
            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
