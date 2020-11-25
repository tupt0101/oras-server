package capstone.oras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "job", schema = "public", catalog = "db67ot35cl90oe")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class JobEntity implements Serializable {
    @ApiModelProperty()
    @JsonProperty("id")
    private int id;
    @ApiModelProperty(example = "Coder")
    @JsonProperty("title")
    private String title;
    @ApiModelProperty(example = "Code a lot")
    @JsonProperty("description")
    private String description;
    @ApiModelProperty(example = "500")
    @JsonProperty("salaryFrom")
    private Double salaryFrom;
    @ApiModelProperty(example = "1500")
    @JsonProperty("salaryTo")
    private Double salaryTo;
    @ApiModelProperty(example = "$")
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("salaryHidden")
    private Boolean salaryHidden;
    @JsonProperty("vacancies")
    private Integer vacancies;
    @ApiModelProperty(example = "2020-09-28")
    @JsonProperty("applyFrom")
    private LocalDateTime applyFrom;
    @ApiModelProperty(example = "2020-10-28")
    @JsonProperty("applyTo")
    private LocalDateTime applyTo;
    @ApiModelProperty(example = "1", value = "should be a valid id")
    @JsonProperty("talentPoolId")
    private Integer talentPoolId;
    @ApiModelProperty(example = "1", value = "should be a valid id")
    @JsonProperty("creatorId")
    private Integer creatorId;
    @ApiModelProperty(example = "Draft")
    @JsonProperty("status")
    private String status;
    @ApiModelProperty(example = "2020-09-28")
    @JsonProperty("createDate")
    private LocalDateTime createDate;
    @ApiModelProperty(hidden = true)
    @JsonProperty("talentPoolByTalentPoolId")
    private TalentPoolEntity talentPoolByTalentPoolId;
    @JsonProperty("accountByCreatorId")
    @ApiModelProperty(hidden = true)
    private AccountEntity accountByCreatorId;
    @ApiModelProperty(hidden = true)
    @JsonProperty("jobApplicationsById")
    private Collection<JobApplicationEntity> jobApplicationsById;
    private String jobType;
    private String location;
    private Integer openjobJobId;
    private String category;
    private String processedJd;

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
    public LocalDateTime getApplyFrom() {
        return applyFrom;
    }

    public void setApplyFrom(LocalDateTime applyFrom) {
        this.applyFrom = applyFrom;
    }

    @Basic
    @Column(name = "apply_to", nullable = true)
    public LocalDateTime getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(LocalDateTime applyTo) {
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
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
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
                Objects.equals(createDate, jobEntity.createDate) &&
                Objects.equals(jobType, jobEntity.jobType) &&
                Objects.equals(location, jobEntity.location) &&
                Objects.equals(openjobJobId, jobEntity.openjobJobId) &&
                Objects.equals(category, jobEntity.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, salaryFrom, salaryTo, currency, salaryHidden, vacancies, applyFrom, applyTo, talentPoolId, creatorId, status, createDate, jobType, location, openjobJobId, category);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "talent_pool_id", referencedColumnName = "id", insertable=false, updatable=false)
//    @JsonManagedReference(value = "job-talent")
    public TalentPoolEntity getTalentPoolByTalentPoolId() {
        return talentPoolByTalentPoolId;
    }

    public void setTalentPoolByTalentPoolId(TalentPoolEntity talentPoolByTalentPoolId) {
        this.talentPoolByTalentPoolId = talentPoolByTalentPoolId;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", insertable=false, updatable=false)
//    @JsonManagedReference (value =  "job-creator")
    public AccountEntity getAccountByCreatorId() {
        return accountByCreatorId;
    }

    public void setAccountByCreatorId(AccountEntity accountByCreatorId) {
        this.accountByCreatorId = accountByCreatorId;
    }

    @OneToMany(mappedBy = "jobByJobId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "application-job")
    public Collection<JobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<JobApplicationEntity> jobApplicationsById) {
        this.jobApplicationsById = jobApplicationsById;
    }

    @Basic
    @Column(name = "job_type")
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "openjob_job_id")
    public Integer getOpenjobJobId() {
        return openjobJobId;
    }

    public void setOpenjobJobId(Integer openjobJobId) {
        this.openjobJobId = openjobJobId;
    }

    @Basic
    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @Column(name = "processed_jd")
    public String getProcessedJd() {
        return processedJd;
    }

    public void setProcessedJd(String processedJd) {
        this.processedJd = processedJd;
    }
}
