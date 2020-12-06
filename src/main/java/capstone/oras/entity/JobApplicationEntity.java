package capstone.oras.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "job_application")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class JobApplicationEntity implements Serializable {
    private int id;
    @ApiModelProperty(example = "1")
    private Integer candidateId;
    private Double matchingRate;
    private String cv;
    @ApiModelProperty(example = "2020-12-23T17:00:00")
    private LocalDateTime applyDate;
    private LocalDateTime hiredDate;

    @ApiModelProperty(example = "linkedin")
    private String source;
    @ApiModelProperty(example = "apply")
    private String status;
    @ApiModelProperty(example = "This guy is good")
    private String comment;
    @ApiModelProperty(example = "1")
    private Integer jobId;
    @ApiModelProperty(hidden = true)
    private CandidateEntity candidateByCandidateId;
    @ApiModelProperty(hidden = true)
    private JobEntity jobByJobId;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "candidate_id", nullable = true)
    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    @Basic
    @Column(name = "matching_rate", nullable = true, precision = 0)
    public Double getMatchingRate() {
        return matchingRate;
    }

    public void setMatchingRate(Double matchingRate) {
        this.matchingRate = matchingRate;
    }

    @Basic
    @Column(name = "cv", nullable = true, length = 2147483647)
    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    @Basic
    @Column(name = "apply_date", nullable = true)
    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    @Basic
    @Column(name = "hired_date", nullable = true)
    public LocalDateTime getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(LocalDateTime hiredDate) {
        this.hiredDate = hiredDate;
    }

    @Basic
    @Column(name = "source", nullable = true, length = 100)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "comment", nullable = true, length = 2147483647)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "job_id", nullable = true)
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobApplicationEntity that = (JobApplicationEntity) o;
        return id == that.id &&
                Objects.equals(candidateId, that.candidateId) &&
                Objects.equals(matchingRate, that.matchingRate) &&
                Objects.equals(cv, that.cv) &&
                Objects.equals(applyDate, that.applyDate) &&
                Objects.equals(source, that.source) &&
                Objects.equals(status, that.status) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                candidateId,
                matchingRate, cv, applyDate,
                source, status, comment,
                jobId
        );
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", insertable=false, updatable=false)
//    @JsonManagedReference (value = "application-candidate")
    public CandidateEntity getCandidateByCandidateId() {
        return candidateByCandidateId;
    }

    public void setCandidateByCandidateId(CandidateEntity candidateByCandidateId) {
        this.candidateByCandidateId = candidateByCandidateId;
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable=false, updatable=false)
//    @JsonManagedReference (value = "application-job")
    public JobEntity getJobByJobId() {
        return jobByJobId;
    }

    public void setJobByJobId(JobEntity jobByJobId) {
        this.jobByJobId = jobByJobId;
    }
}
