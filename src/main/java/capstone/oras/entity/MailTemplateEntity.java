package capstone.oras.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MailTemplate", schema = "dbo", catalog = "ORAS")
public class MailTemplateEntity {
    private int id;
    private String subject;
    private String body;
    private String type;
    private Integer creatorId;
    private AccountEntity accountByCreatorId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "subject", nullable = true, length = 100)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "body", nullable = true, length = 2147483647)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "creatorId", nullable = true)
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailTemplateEntity that = (MailTemplateEntity) o;
        return id == that.id &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(body, that.body) &&
                Objects.equals(type, that.type) &&
                Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, body, type, creatorId);
    }

    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    public AccountEntity getAccountByCreatorId() {
        return accountByCreatorId;
    }

    public void setAccountByCreatorId(AccountEntity accountByCreatorId) {
        this.accountByCreatorId = accountByCreatorId;
    }
}
