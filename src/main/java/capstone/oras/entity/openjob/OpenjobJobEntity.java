package capstone.oras.entity.openjob;

import java.sql.Date;
import java.util.Objects;

public class OpenjobJobEntity {
    private Integer id;
    private String title;
    private String description;
    private Date applyTo;
    private Date createDate;
    private Integer companyId;
    private String currency;
    private Double salaryFrom;
    private Boolean salaryHidden;
    private Double salaryTo;
    private String status;
    private Integer vacancies;
    private String jobType;
    private String category;
    private String skill;
    private String location;
    private Integer accountId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Date applyTo) {
        this.applyTo = applyTo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Double salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Boolean getSalaryHidden() {
        return salaryHidden;
    }

    public void setSalaryHidden(Boolean salaryHidden) {
        this.salaryHidden = salaryHidden;
    }

    public Double getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Double salaryTo) {
        this.salaryTo = salaryTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenjobJobEntity that = (OpenjobJobEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(applyTo, that.applyTo) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(salaryFrom, that.salaryFrom) &&
                Objects.equals(salaryHidden, that.salaryHidden) &&
                Objects.equals(salaryTo, that.salaryTo) &&
                Objects.equals(status, that.status) &&
                Objects.equals(vacancies, that.vacancies) &&
                Objects.equals(jobType, that.jobType) &&
                Objects.equals(category, that.category) &&
                Objects.equals(skill, that.skill) &&
                Objects.equals(location, that.location) &&
                Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, applyTo, createDate, companyId, currency, salaryFrom, salaryHidden, salaryTo, status, vacancies, jobType, category, skill, location, accountId);
    }

    @Override
    public String toString() {
        return "OpenjobJobEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", applyTo=" + applyTo +
                ", createDate=" + createDate +
                ", companyId=" + companyId +
                ", currency='" + currency + '\'' +
                ", salaryFrom=" + salaryFrom +
                ", salaryHidden=" + salaryHidden +
                ", salaryTo=" + salaryTo +
                ", status='" + status + '\'' +
                ", vacancies=" + vacancies +
                ", jobType='" + jobType + '\'' +
                ", category='" + category + '\'' +
                ", skill='" + skill + '\'' +
                ", location='" + location + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
