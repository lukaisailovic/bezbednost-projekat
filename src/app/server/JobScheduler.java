package app.server;

import app.shared.Job;
import app.shared.Parameters;

import java.util.ArrayList;
import java.util.List;

public class JobScheduler {

    private final int maxLength;
    private final String hash;

    public JobScheduler(int maxLength, String hash) {
        this.maxLength = maxLength;
        this.hash = hash;
    }

    public List<Job> getJobs(){
        ArrayList<Job> jobs = new ArrayList<>();
        for (int i = 1; i <= maxLength; i++){
            for (int j = 0; j < Parameters.CHARSET.length(); j++){
                jobs.add(new Job(i,j, hash));
            }
        }
        return jobs;
    }
}
