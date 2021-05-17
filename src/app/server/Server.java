package app.server;

import app.shared.Job;
import app.shared.Parameters;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Server {

    private static final int PORT = 2021;
    private final ServerSocket serverSocket;
    private final BlockingQueue<Job> jobsQueue = new LinkedBlockingQueue<>();
    private final AtomicLong checks = new AtomicLong(0);
    private final ThreadPoolExecutor clients = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private final String target;
    private final AtomicBoolean solved = new AtomicBoolean(false);

    public Server() throws Exception {
        this.serverSocket = new ServerSocket(PORT);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter hash value: ");
        this.target = scanner.nextLine();
        if (this.target == null || this.target.length() == 0){
            System.out.println("Value not provided, exiting...");
            return;
        }
        this.populateJobQueue();
    }

    public BlockingQueue<Job> getJobsQueue() {
        return jobsQueue;
    }

    public String getTarget() {
        return target;
    }


    public AtomicBoolean getSolved() {
        return solved;
    }

    private void populateJobQueue(){
        JobScheduler jobScheduler = new JobScheduler(Parameters.MAX_PASSWORD_LENGTH, target);
        jobsQueue.addAll(jobScheduler.getJobs());
    }
    public void start() throws IOException {
        System.out.println("Server waiting for connections on port "+PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, this,checks);
            clients.submit(serverThread);
        }
    }
    public void solveHash(String cleartext){
        this.clients.shutdownNow();
        System.out.println("Solution for hash "+this.target+" found");
        System.out.println("Cleartext value: "+cleartext);
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
