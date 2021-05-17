package app;



import app.server.Benchmark;
import app.shared.Parameters;
import core.SHA1;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static final int LEN = 8;

    private static AtomicLong checks = new AtomicLong(0);
    private static char[] charset = Parameters.CHARSET.toCharArray();
    public static void main(String[] args) throws Exception {


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Benchmark benchmark = new Benchmark(checks);
        executor.scheduleAtFixedRate(benchmark,0,1, TimeUnit.SECONDS);
        for (long i =0; i < 100; i++){
            generate("",LEN);
        }

    }

    public static void generate(String str, int length){
        if (length == 0) {
            //System.out.println(str);
            String value = SHA1.hash(str);
            checks.incrementAndGet();
            return;
        }

        for (int i = 0; i < charset.length; i++){
            generate(str + charset[i],length -1);
        }
    }

}
