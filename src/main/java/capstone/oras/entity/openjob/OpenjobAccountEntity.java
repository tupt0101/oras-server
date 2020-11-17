package capstone.oras.entity.openjob;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

public class OpenjobAccountEntity {
    private Integer id;
    private String email;
    private String password;
    private String role;
    private Boolean confirmation;
    private Integer companyId;
    private String phoneNo;
    private String cv;
    private String fullname;
    @ApiModelProperty(hidden = true)
    private Collection<OpenjobJobApplicationEntity> jobApplicationsById;
    private String address;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "confirmation")
    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }


    @Basic
    @Column(name = "company_id")
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
    @Column(name = "cv")
    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }


    @OneToMany(mappedBy = "accountByAccountId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value = "pppp")
    public Collection<OpenjobJobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<OpenjobJobApplicationEntity> jobApplicationByIds) {
        this.jobApplicationsById = jobApplicationByIds;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenjobAccountEntity that = (OpenjobAccountEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(role, that.role) &&
                Objects.equals(confirmation, that.confirmation) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(cv, that.cv) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, confirmation, companyId, phoneNo, cv, fullname, address);
    }
}
