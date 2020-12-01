package capstone.oras.api.report.model;

import capstone.oras.entity.CandidateEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@Setter
public class TimeToHire implements Serializable {
    private String jobTitle;
    private CandidateEntity hiredCandidate;
    private LocalDateTime applyDate;
    private LocalDateTime hiredDate;
    private Period timeToHired;

    public TimeToHire() {
    }
}
