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
        if (checks< 1000){
            System.out.println(checks+" H/s");
        } else if( checks > 1000 && checks < 1000000){
            System.out.println((float) checks/1000.0+" kH/s");
        } else {
            System.out.println((float) checks/1000000.0+" MH/s");
        }
        lastStoredHashrate.set(checks);
        this.checks.set(0);
    }
}
