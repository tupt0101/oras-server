package capstone.oras.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Job", schema = "dbo", catalog = "ORAS")
public class JobEntity {
    private Integer id;
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

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "salaryFrom")
    public Double getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Double salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    @Basic
    @Column(name = "salaryTo")
    public Double getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Double salaryTo) {
        this.salaryTo = salaryTo;
    }

    @Basic
    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Basic
    @Column(name = "salaryHidden")
    public Boolean getSalaryHidden() {
        return salaryHidden;
    }

    public void setSalaryHidden(Boolean salaryHidden) {
        this.salaryHidden = salaryHidden;
    }

    @Basic
    @Column(name = "vacancies")
    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    @Basic
    @Column(name = "applyFrom")
    public Date getApplyFrom() {
        return applyFrom;
    }

    public void setApplyFrom(Date applyFrom) {
        this.applyFrom = applyFrom;
    }

    @Basic
    @Column(name = "applyTo")
    public Date getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Date applyTo) {
        this.applyTo = applyTo;
    }

    @Basic
    @Column(name = "talentPoolId")
    public Integer getTalentPoolId() {
        return talentPoolId;
    }

    public void setTalentPoolId(Integer talentPoolId) {
        this.talentPoolId = talentPoolId;
    }

    @Basic
    @Column(name = "creatorId")
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createDate")
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
        return Objects.equals(id, jobEntity.id) &&
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
}
