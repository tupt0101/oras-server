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

}
