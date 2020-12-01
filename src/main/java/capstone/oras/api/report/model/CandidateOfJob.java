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
}
