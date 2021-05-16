package app.client;

import app.shared.Job;

import java.util.concurrent.BlockingQueue;

public class JobConsumer implements Runnable{
    private final BlockingQueue<String> sendingQueue;
    private final Job job;

    public JobConsumer(BlockingQueue<String> sendingQueue, Job job) {
        this.sendingQueue = sendingQueue;
        this.job = job;
    }

    @Override
    public void run() {
        Bruteforce bruteforce = new Bruteforce(sendingQueue,job.getStartPos());
        bruteforce.generate("",job.getLength());
        System.out.println("JOB COMPLETED");
        System.out.println("GENERATED: "+bruteforce.getChecked());
    }
}
