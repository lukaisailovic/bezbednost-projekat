package app.server;

import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark implements Runnable{

    private final AtomicInteger checks;

    public Benchmark(AtomicInteger checks) {
        this.checks = checks;
    }

    @Override
    public void run() {
        System.out.println(checks.get()+" h/s");
        checks.set(0);
    }
}
