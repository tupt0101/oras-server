package capstone.oras.entity.openjob;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class OpenjobJobApplicationEntity {
    private Integer id;
    private Integer jobId;
    private Integer accountId;
    @ApiModelProperty(hidden = true)
    private OpenjobAccountEntity accountByAccountId;
    private LocalDateTime applyAt;
    private String cv;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "job_id")
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Basic
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }


//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable=false, updatable=false)
    public OpenjobAccountEntity getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(OpenjobAccountEntity accountId) {
        this.accountByAccountId = accountId;
    }

    @Basic
    @Column(name = "apply_at")
    public LocalDateTime getApplyAt() {
        return applyAt;
    }


    public void setApplyAt(LocalDateTime applyAt) {
        this.applyAt = applyAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenjobJobApplicationEntity that = (OpenjobJobApplicationEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(jobId, that.jobId) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(applyAt, that.applyAt) &&
                Objects.equals(cv, that.cv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobId, accountId, applyAt, cv);
    }

    @Basic
    @Column(name = "cv")
    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }
}
