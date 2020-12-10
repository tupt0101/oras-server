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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public CandidateEntity getHiredCandidate() {
        return hiredCandidate;
    }

    public void setHiredCandidate(CandidateEntity hiredCandidate) {
        this.hiredCandidate = hiredCandidate;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(LocalDateTime hiredDate) {
        this.hiredDate = hiredDate;
    }

    public Long getTimeToHired() {
        return timeToHired;
    }

    public void setTimeToHired(Long timeToHired) {
        this.timeToHired = timeToHired;
    }
}
