package capstone.oras.api.report.model;

import capstone.oras.entity.JobEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateOfJob {
    private JobEntity job;
    private int totalApplication;
    private int hired;

    public CandidateOfJob() {
    }

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
    }

    public int getTotalApplication() {
        return totalApplication;
    }

    public void setTotalApplication(int totalApplication) {
        this.totalApplication = totalApplication;
    }

    public int getHired() {
        return hired;
    }

    public void setHired(int hired) {
        this.hired = hired;
    }
}
