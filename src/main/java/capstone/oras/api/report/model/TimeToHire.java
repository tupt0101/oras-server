package capstone.oras.api.report.model;

import capstone.oras.entity.CandidateEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class TimeToHire implements Serializable {
    private String jobTitle;
    private CandidateEntity hiredCandidate;
    private LocalDateTime applyDate;
    private LocalDateTime hiredDate;
    private Long timeToHired;

    public TimeToHire() {
    }
}
