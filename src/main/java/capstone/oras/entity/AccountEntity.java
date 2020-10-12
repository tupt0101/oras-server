package capstone.oras.entity;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "account", schema = "dbo", catalog = "ORAS")
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AccountEntity implements Serializable {
    private int id;
    @ApiModelProperty(example = "example@mail.com")
    private String email;
    @ApiModelProperty(example = "123456")
    private String password;
    @ApiModelProperty(example = "Nguyen Nhan Cu")
    private String fullname;
    private Boolean active;
    @ApiModelProperty(hidden = true)
    private Collection<JobEntity> jobsById;
    @ApiModelProperty(hidden = true)
    private Collection<MailTemplateEntity> mailTemplatesById;



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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, fullname, active);
    }

    @OneToMany(mappedBy = "accountByCreatorId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference (value =  "job-creator")
    public Collection<JobEntity> getJobsById() {
        return jobsById;
    }

    public void setJobsById(Collection<JobEntity> jobsById) {
        this.jobsById = jobsById;
    }

    @OneToMany(mappedBy = "accountByCreatorId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference (value = "mail-creator")
    public Collection<MailTemplateEntity> getMailTemplatesById() {
        return mailTemplatesById;
    }

    public void setMailTemplatesById(Collection<MailTemplateEntity> mailTemplatesById) {
        this.mailTemplatesById = mailTemplatesById;
    }
}
