package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemStatistic {
    private int totalJobs;
    private int openJobs;
    private int candidate;
    private int user;

    public SystemStatistic() {
    }

    public int getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(int totalJobs) {
        this.totalJobs = totalJobs;
    }

    public int getOpenJobs() {
        return openJobs;
    }

    public void setOpenJobs(int openJobs) {
        this.openJobs = openJobs;
    }

    public int getCandidate() {
        return candidate;
    }

    public void setCandidate(int candidate) {
        this.candidate = candidate;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
