package capstone.oras.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "job_application", schema = "dbo", catalog = "ORAS")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class JobApplicationEntity implements Serializable {
    private int id;
    private Integer candidateId;
    private Double matchingRate;
    private String cv;
    private Date applyDate;
    private Integer talentPoolId;
    private String source;
    private String status;
    private String comment;
    private Integer jobId;
    private CandidateEntity candidateByCandidateId;
    private TalentPoolEntity talentPoolByTalentPoolId;
    private JobEntity jobByJobId;

    @Id
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
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Basic
    @Column(name = "talent_pool_id", nullable = true)
    public Integer getTalentPoolId() {
        return talentPoolId;
    }

    public void setTalentPoolId(Integer talentPoolId) {
        this.talentPoolId = talentPoolId;
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
                Objects.equals(talentPoolId, that.talentPoolId) &&
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
                talentPoolId,
                source, status, comment,
                jobId
        );
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", insertable=false, updatable=false)
    @JsonManagedReference
    public CandidateEntity getCandidateByCandidateId() {
        return candidateByCandidateId;
    }

    public void setCandidateByCandidateId(CandidateEntity candidateByCandidateId) {
        this.candidateByCandidateId = candidateByCandidateId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_pool_id", referencedColumnName = "id", insertable=false, updatable=false)
    @JsonManagedReference
    public TalentPoolEntity getTalentPoolByTalentPoolId() {
        return talentPoolByTalentPoolId;
    }

    public void setTalentPoolByTalentPoolId(TalentPoolEntity talentPoolByTalentPoolId) {
        this.talentPoolByTalentPoolId = talentPoolByTalentPoolId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable=false, updatable=false)
    @JsonManagedReference
    public JobEntity getJobByJobId() {
        return jobByJobId;
    }

    public void setJobByJobId(JobEntity jobByJobId) {
        this.jobByJobId = jobByJobId;
    }
}
