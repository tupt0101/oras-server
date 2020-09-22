package capstone.oras.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Candidate", schema = "dbo", catalog = "ORAS")
public class CandidateEntity {
    private Integer id;
    private String fullname;
    private String email;
    private String phoneNo;
    private String address;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fullname")
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
    @Column(name = "phoneNo")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Basic
    @Column(name = "address")
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
        CandidateEntity that = (CandidateEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, email, phoneNo, address);
    }
}
