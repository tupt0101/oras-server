package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateOfJob {
    private String jobTitle;
    private int totalApplication;
    private int hired;

    public CandidateOfJob() {
    }
}
