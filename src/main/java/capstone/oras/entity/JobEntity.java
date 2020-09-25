package capstone.oras.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "job", schema = "dbo", catalog = "ORAS")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class JobEntity implements Serializable {
    private int id;
    private String title;
    private String description;
    private Double salaryFrom;
    private Double salaryTo;
    private String currency;
    private Boolean salaryHidden;
    private Integer vacancies;
    private Date applyFrom;
    private Date applyTo;
    private Integer talentPoolId;
    private Integer creatorId;
    private String status;
    private Date createDate;
    private TalentPoolEntity talentPoolByTalentPoolId;
    private AccountEntity accountByCreatorId;
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
    @Column(name = "title", nullable = true, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 2147483647)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "salary_from", nullable = true, precision = 0)
    public Double getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Double salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    @Basic
    @Column(name = "salary_to", nullable = true, precision = 0)
    public Double getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Double salaryTo) {
        this.salaryTo = salaryTo;
    }

    @Basic
    @Column(name = "currency", nullable = true, length = 50)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Basic
    @Column(name = "salary_hidden", nullable = true)
    public Boolean getSalaryHidden() {
        return salaryHidden;
    }

    public void setSalaryHidden(Boolean salaryHidden) {
        this.salaryHidden = salaryHidden;
    }

    @Basic
    @Column(name = "vacancies", nullable = true)
    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    @Basic
    @Column(name = "apply_from", nullable = true)
    public Date getApplyFrom() {
        return applyFrom;
    }

    public void setApplyFrom(Date applyFrom) {
        this.applyFrom = applyFrom;
    }

    @Basic
    @Column(name = "apply_to", nullable = true)
    public Date getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Date applyTo) {
        this.applyTo = applyTo;
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
    @Column(name = "creator_id", nullable = true)
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
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
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobEntity jobEntity = (JobEntity) o;
        return id == jobEntity.id &&
                Objects.equals(title, jobEntity.title) &&
                Objects.equals(description, jobEntity.description) &&
                Objects.equals(salaryFrom, jobEntity.salaryFrom) &&
                Objects.equals(salaryTo, jobEntity.salaryTo) &&
                Objects.equals(currency, jobEntity.currency) &&
                Objects.equals(salaryHidden, jobEntity.salaryHidden) &&
                Objects.equals(vacancies, jobEntity.vacancies) &&
                Objects.equals(applyFrom, jobEntity.applyFrom) &&
                Objects.equals(applyTo, jobEntity.applyTo) &&
                Objects.equals(talentPoolId, jobEntity.talentPoolId) &&
                Objects.equals(creatorId, jobEntity.creatorId) &&
                Objects.equals(status, jobEntity.status) &&
                Objects.equals(createDate, jobEntity.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, salaryFrom, salaryTo, currency, salaryHidden, vacancies, applyFrom, applyTo, talentPoolId, creatorId, status, createDate);
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
    @JoinColumn(name = "creator_id", referencedColumnName = "id", insertable=false, updatable=false)
    @JsonManagedReference
    public AccountEntity getAccountByCreatorId() {
        return accountByCreatorId;
    }

    public void setAccountByCreatorId(AccountEntity accountByCreatorId) {
        this.accountByCreatorId = accountByCreatorId;
    }

    @OneToMany(mappedBy = "jobByJobId", fetch = FetchType.LAZY)
    @JsonBackReference
    public Collection<JobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<JobApplicationEntity> jobApplicationsById) {
        this.jobApplicationsById = jobApplicationsById;
    }
}
