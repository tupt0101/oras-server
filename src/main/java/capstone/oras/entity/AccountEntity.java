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
@Table(name = "account", schema = "public", catalog = "db67ot35cl90oe")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AccountEntity implements Serializable {
    private int id;
    @ApiModelProperty(example = "example@mail.com")
    private String email;
    @ApiModelProperty(example = "123456")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ApiModelProperty(example = "Nguyen Nhan Cu")
    private String fullname;
    private Boolean confirmMail;
    private Boolean active;
    @ApiModelProperty(example = "admin")
    private String role;
    @ApiModelProperty(hidden = true)
    private Collection<JobEntity> jobsById;
//    @ApiModelProperty(hidden = true)
//    private Collection<MailTemplateEntity> mailTemplatesById;
    private Integer companyId;
    @ApiModelProperty(hidden = true)
    private CompanyEntity companyById;
    @ApiModelProperty(hidden = true)
    private Collection<PurchaseEntity> purchasesById;
    @ApiModelProperty(hidden = true)
    private Collection<ActivityEntity> activitiesById;
    @ApiModelProperty(hidden = true)
    private Collection<AccountPackageEntity> accountPackageById;
    @ApiModelProperty(example = "0913144842")
    private String phoneNo;
    @ApiModelProperty(example = "2020-12-23T17:00:00")
    private LocalDateTime createDate;

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
    @Column(name = "email", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 70)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "fullname", nullable = true, length = 50)
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "active", nullable = true)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "confirm_mail", nullable = true)
    public Boolean getConfirmMail() {
        return confirmMail;
    }

    public void setConfirmMail(Boolean confirmMail) {
        this.confirmMail = confirmMail;
    }

    @Basic
    @Column(name = "role", nullable = true, length = 20)
    public String getRole() {
        return role;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AccountEntity that = (AccountEntity) o;
//        return id == that.id &&
//                Objects.equals(email, that.email) &&
//                Objects.equals(password, that.password) &&
//                Objects.equals(fullname, that.fullname) &&
//                Objects.equals(active, that.active);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, email, password, fullname, active);
//    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(active, that.active) &&
                Objects.equals(role, that.role) &&
                Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, fullname, active, role, companyId);
    }

    @OneToMany(mappedBy = "accountByCreatorId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference (value =  "job-creator")
    public Collection<JobEntity> getJobsById() {
        return jobsById;
    }

//    @OneToMany(mappedBy = "accountByCreatorId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonBackReference (value = "mail-creator")
//    public Collection<MailTemplateEntity> getMailTemplatesById() {
//        return mailTemplatesById;
//    }
//
//    public void setMailTemplatesById(Collection<MailTemplateEntity> mailTemplatesById) {
//        this.mailTemplatesById = mailTemplatesById;
//    }

    public void setJobsById(Collection<JobEntity> jobsById) {
        this.jobsById = jobsById;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable=false, updatable=false)
    public CompanyEntity getCompanyById() {
        return companyById;
    }

    public void setCompanyById(CompanyEntity companyById) {
        this.companyById = companyById;
    }

    @Basic
    @Column(name = "company_id")
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @OneToMany(mappedBy = "accountById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference (value = "account-purchase")
    public Collection<PurchaseEntity> getPurchasesById() {
        return purchasesById;
    }

    public void setPurchasesById(Collection<PurchaseEntity> purchasesById) {
        this.purchasesById = purchasesById;
    }

    @OneToMany(mappedBy = "accountById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference (value = "account-activity")
    public Collection<ActivityEntity> getActivitiesById() {
        return activitiesById;
    }

    public void setActivitiesById(Collection<ActivityEntity> activitiesById) {
        this.activitiesById = activitiesById;
    }

    @OneToMany(mappedBy = "accountById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "account-accountpackage")
    public Collection<AccountPackageEntity> getAccountPackageById() {
        return accountPackageById;
    }

    public void setAccountPackageById(Collection<AccountPackageEntity> accountPackageById) {
        this.accountPackageById = accountPackageById;
    }

    @Basic
    @Column(name = "phone_no")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
