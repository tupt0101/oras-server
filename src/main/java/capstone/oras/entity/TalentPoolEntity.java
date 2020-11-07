package capstone.oras.entity;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "talent_pool", schema = "public", catalog = "db67ot35cl90oe")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class TalentPoolEntity implements Serializable {
    private int id;
    @ApiModelProperty(example = "Back-end Developer")
    private String name;
    @ApiModelProperty(hidden = true)
    private Collection<JobEntity> jobsById;
    @ApiModelProperty(hidden = true)
    private Collection<JobApplicationEntity> jobApplicationsById;

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
    @Column(name = "name", nullable = true, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalentPoolEntity that = (TalentPoolEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @OneToMany(mappedBy = "talentPoolByTalentPoolId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "job-talent")
    public Collection<JobEntity> getJobsById() {
        return jobsById;
    }

    public void setJobsById(Collection<JobEntity> jobsById) {
        this.jobsById = jobsById;
    }

    @OneToMany(mappedBy = "talentPoolByTalentPoolId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "application-talent")
    public Collection<JobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<JobApplicationEntity> jobApplicationsById) {
        this.jobApplicationsById = jobApplicationsById;
    }
}
