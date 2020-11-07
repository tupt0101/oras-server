package capstone.oras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "candidate", schema = "public", catalog = "db67ot35cl90oe")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class CandidateEntity implements Serializable {
    private int id;
    @ApiModelProperty(example = "Tran Trong Tuan")
    private String fullname;
    @ApiModelProperty(example = "example@mail.com")
    private String email;
    @ApiModelProperty(example = "089512344")
    private String phoneNo;
    @ApiModelProperty(example = "56 Nguyen Phu Trong Q.05")
    private String address;
    @ApiModelProperty(hidden = true)
    private Collection<JobApplicationEntity> jobApplicationsById;

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
    @Column(name = "fullname", nullable = true, length = 50)
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 90)
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
    @Column(name = "address", nullable = true, length = 2147483647)
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
        return id == that.id &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNo, that.phoneNo) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, email, phoneNo, address);
    }

    @OneToMany(mappedBy = "candidateByCandidateId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "application-candidate")
    public Collection<JobApplicationEntity> getJobApplicationsById() {
        return jobApplicationsById;
    }

    public void setJobApplicationsById(Collection<JobApplicationEntity> jobApplicationsById) {
        this.jobApplicationsById = jobApplicationsById;
    }
}
