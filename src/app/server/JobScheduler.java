package app.server;

import app.shared.Job;
import app.shared.Parameters;

import java.util.ArrayList;
import java.util.List;

public class JobScheduler {

    private final int maxLength;

    public JobScheduler(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<Job> getJobs(){
        ArrayList<Job> jobs = new ArrayList<>();
        for (int i = 1; i <= maxLength; i++){
            for (int j = 0; j < Parameters.CHARSET.length(); j++){
                jobs.add(new Job(i,j));
            }
        }
        return jobs;
    }
}
