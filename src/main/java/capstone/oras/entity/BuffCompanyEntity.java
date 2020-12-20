package capstone.oras.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "buff_company", schema = "public", catalog = "oras")
public class BuffCompanyEntity {
    private int id;
    private String name;
    private String location;
    private String taxCode;
    private String email;
    private String phoneNo;
    private String description;
    private String avatar;
    private Integer openjobCompanyId;
    private Boolean verified;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "location", nullable = true, length = 200)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "tax_code", nullable = true, length = 20)
    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone_no", nullable = true, length = 20)
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "avatar", nullable = true, length = 200)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "openjob_company_id", nullable = true)
    public Integer getOpenjobCompanyId() {
        return openjobCompanyId;
    }

    public void setOpenjobCompanyId(Integer openjobCompanyId) {
        this.openjobCompanyId = openjobCompanyId;
    }

    @Basic
    @Column(name = "verified", nullable = true)
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuffCompanyEntity that = (BuffCompanyEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(taxCode, that.taxCode) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(description, that.description) &&
                Objects.equals(avatar, that.avatar) &&
                Objects.equals(openjobCompanyId, that.openjobCompanyId) &&
                Objects.equals(verified, that.verified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, taxCode, email, phoneNo, description, avatar, openjobCompanyId, verified);
    }
}
