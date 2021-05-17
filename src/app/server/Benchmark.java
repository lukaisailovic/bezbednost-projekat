package app.server;


import java.util.concurrent.atomic.AtomicLong;

public class Benchmark implements Runnable{

    private final AtomicLong checks;
    private final AtomicLong lastStoredHashrate;

    public Benchmark(AtomicLong checks, AtomicLong lastStoredHashrate) {
        this.checks = checks;
        this.lastStoredHashrate = lastStoredHashrate;
    }

    @Override
    public void run() {
        long checks = this.checks.get();
        System.out.println(checks+" h/s");
        lastStoredHashrate.set(checks);
        this.checks.set(0);
    }
}
