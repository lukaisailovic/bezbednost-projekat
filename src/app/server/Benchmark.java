package app.server;


import java.util.concurrent.atomic.AtomicLong;

public class Benchmark implements Runnable{

    private final AtomicLong checks;

    public Benchmark(AtomicLong checks) {
        this.checks = checks;
    }

    @Override
    public void run() {
        System.out.println(checks.get()+" h/s");
        checks.set(0);
    }
}
