package app;


import app.client.Bruteforce;
import app.shared.Job;
import app.server.JobScheduler;

public class Main {
    private static final int LEN = 4;


    public static void main(String[] args) throws Exception {


//        Bruteforce bruteforce = new Bruteforce("test",0);
//        bruteforce.generate("",LEN);
//
//        System.out.println("MAX COMBINATIONS: " + (long)Math.pow(bruteforce.getCharsetLength(),LEN));
//        System.out.println("CHECKED: "+bruteforce.getChecked());
        JobScheduler jobScheduler = new JobScheduler(4);
        for (Job job: jobScheduler.getJobs()){
            Bruteforce bruteforce = new Bruteforce("test",job.getStartPos());
            System.out.println(job);
            bruteforce.generate("",job.getLength());
            System.out.println("JOB COMPLETED ");
            System.out.println("CHECKED: "+bruteforce.getChecked());
        }


    }

}
