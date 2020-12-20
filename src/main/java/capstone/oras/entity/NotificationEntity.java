package capstone.oras.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification", schema = "public", catalog = "db67ot35cl90oe")
public class NotificationEntity {
    private int id;
    private Integer receiverId;
    private String type;
    private Integer targetId;
    private LocalDateTime createDate;
    private Boolean isNew;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "receiver_id")
    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "target_id")
    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    @Basic
    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "is_new")
    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationEntity that = (NotificationEntity) o;
        return id == that.id &&
                Objects.equals(receiverId, that.receiverId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(targetId, that.targetId) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(isNew, that.isNew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, receiverId, type, targetId, createDate, isNew);
    }
}
