package capstone.oras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "company", schema = "public", catalog = "db67ot35cl90oe")
public class CompanyEntity {
    private int id;
    private String name;
    private String location;
    private String taxCode;
    private String email;
    private String phoneNo;
    private String description;
    private String avatar;
    @ApiModelProperty(hidden = true)
    private AccountEntity accountById;
    @ApiModelProperty(hidden = true)
    private Integer openjobCompanyId;
    private Boolean verified;
    private LocalDateTime modifyDate;

    @Basic
    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "tax_code")
    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone_no")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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
    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity that = (CompanyEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(taxCode, that.taxCode) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(avatar, that.avatar) &&
                Objects.equals(openjobCompanyId, that.openjobCompanyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, taxCode, email, phoneNo, description, avatar, openjobCompanyId);
    }

    @OneToOne(mappedBy = "companyById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "company-account")
    public AccountEntity getAccountById() {
        return accountById;
    }

    public void setAccountById(AccountEntity accountById) {
        this.accountById = accountById;
    }

    @Basic
    @Column(name = "openjob_company_id")
    public Integer getOpenjobCompanyId() {
        return openjobCompanyId;
    }

    public void setOpenjobCompanyId(Integer openjobCompanyId) {
        this.openjobCompanyId = openjobCompanyId;
    }

    @Basic
    @Column(name = "verified")
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
