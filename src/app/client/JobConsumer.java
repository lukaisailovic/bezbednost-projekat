package app.client;

import app.shared.Job;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class JobConsumer implements Runnable{
    private final BlockingQueue<String> sendingQueue;
    private final Job job;
    private final AtomicLong hashrate;

    public JobConsumer(BlockingQueue<String> sendingQueue, Job job, AtomicLong hashrate) {
        this.sendingQueue = sendingQueue;
        this.job = job;

        this.hashrate = hashrate;
    }

    @Override
    public void run() {
        Bruteforce bruteforce = new Bruteforce(sendingQueue,job.getStartPos(),job.getHash(),hashrate);
        bruteforce.generate("",job.getLength());

    }
}
