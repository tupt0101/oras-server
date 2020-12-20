package capstone.oras.model.custom;

import capstone.oras.entity.BuffCompanyEntity;
import capstone.oras.entity.CompanyEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AccountBuffModel {
    private int id;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String fullname;
    private Boolean confirmMail;
    private Boolean active;
    private String role;
    private Integer companyId;
    private CompanyEntity companyById;
    private String phoneNo;
    private LocalDateTime createDate;
    private BuffCompanyEntity buffCompany;

    public AccountBuffModel() {
    }

    public AccountBuffModel(int id, String email, String password, String fullname, Boolean confirmMail, Boolean active, String role, Integer companyId, CompanyEntity companyById, String phoneNo, LocalDateTime createDate, BuffCompanyEntity buffCompany) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.confirmMail = confirmMail;
        this.active = active;
        this.role = role;
        this.companyId = companyId;
        this.companyById = companyById;
        this.phoneNo = phoneNo;
        this.createDate = createDate;
        this.buffCompany = buffCompany;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Boolean getConfirmMail() {
        return confirmMail;
    }

    public void setConfirmMail(Boolean confirmMail) {
        this.confirmMail = confirmMail;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public CompanyEntity getCompanyById() {
        return companyById;
    }

    public void setCompanyById(CompanyEntity companyById) {
        this.companyById = companyById;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public BuffCompanyEntity getBuffCompany() {
        return buffCompany;
    }

    public void setBuffCompany(BuffCompanyEntity buffCompany) {
        this.buffCompany = buffCompany;
    }
}
