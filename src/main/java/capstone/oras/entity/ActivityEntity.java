package capstone.oras.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "activity", schema = "public", catalog = "db67ot35cl90oe")
public class ActivityEntity {
    private int id;
    private String title;
    private int creatorId;
    private LocalDateTime time;
    @ApiModelProperty(hidden = true)
    private AccountEntity accountById;
    private Integer jobId;
    @ApiModelProperty(hidden = true)
    private JobEntity jobById;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 2147483647)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "creator_id", nullable = true)
    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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
        ActivityEntity that = (ActivityEntity) o;
        return id == that.id &&
                creatorId == that.creatorId &&
                jobId == that.jobId &&
                Objects.equals(title, that.title) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, creatorId, time, jobId);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", insertable=false, updatable=false)
    public AccountEntity getAccountById() {
        return accountById;
    }

    public void setAccountById(AccountEntity accountById) {
        this.accountById = accountById;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable=false, updatable=false)
    public JobEntity getJobById() {
        return jobById;
    }

    public void setJobById(JobEntity jobById) {
        this.jobById = jobById;
    }
}
