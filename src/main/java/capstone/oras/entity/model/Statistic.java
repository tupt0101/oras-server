package capstone.oras.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic {
    private int totalJob;
    private int totalPublishJob;
    private int totalCandidate;
    private int totalHiredCandidate;

    public Statistic() {
    }

    public int getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(int totalJob) {
        this.totalJob = totalJob;
    }

    public int getTotalPublishJob() {
        return totalPublishJob;
    }

    public void setTotalPublishJob(int totalPublishJob) {
        this.totalPublishJob = totalPublishJob;
    }

    public int getTotalCandidate() {
        return totalCandidate;
    }

    public void setTotalCandidate(int totalCandidate) {
        this.totalCandidate = totalCandidate;
    }

    public int getTotalHiredCandidate() {
        return totalHiredCandidate;
    }

    public void setTotalHiredCandidate(int totalHiredCandidate) {
        this.totalHiredCandidate = totalHiredCandidate;
    }
}
