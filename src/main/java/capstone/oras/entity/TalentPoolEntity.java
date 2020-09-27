package capstone.oras.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "talent_pool", schema = "dbo", catalog = "ORAS")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class TalentPoolEntity implements Serializable {
    private int id;
    private String name;
    private Collection<JobEntity> jobsById;
    private Collection<JobApplicationEntity> jobApplicationsById;

    @Id
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

    @OneToMany(mappedBy = "talentPoolByTalentPoolId", fetch = FetchType.LAZY)
    @JsonBackReference
    public Collection<JobEntity> getJobsById() {
        return jobsById;
    }

    public void setJobsById(Collection<JobEntity> jobsById) {
        this.jobsById = jobsById;
    }

    @OneToMany(mappedBy = "talentPoolByTalentPoolId", fetch = FetchType.LAZY)
    @JsonBackReference
    public Collection<JobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<JobApplicationEntity> jobApplicationsById) {
        this.jobApplicationsById = jobApplicationsById;
    }
}
