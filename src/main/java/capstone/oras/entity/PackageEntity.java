package capstone.oras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "package", schema = "public", catalog = "db67ot35cl90oe")
public class PackageEntity {
    private int id;
    private String name;
    private Integer numOfPost;
    private Double price;
    private String duration;
    private String currency;
    private String description;
    private String tag;
    private boolean active;
    @ApiModelProperty(hidden = true)
    private Collection<AccountPackageEntity> accountPackagesById;

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
    @Column(name = "num_of_post")
    public Integer getNumOfPost() {
        return numOfPost;
    }

    public void setNumOfPost(Integer numOfPost) {
        this.numOfPost = numOfPost;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "duration")
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "tag")
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageEntity that = (PackageEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(numOfPost, that.numOfPost) &&
                Objects.equals(price, that.price) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numOfPost, price, duration, currency, description);
    }

    @Basic
    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @OneToMany(mappedBy = "accountById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "package-accountpackage")
    public Collection<AccountPackageEntity> getAccountPackagesById() {
        return accountPackagesById;
    }

    public void setAccountPackagesById(Collection<AccountPackageEntity> accountPackagesById) {
        this.accountPackagesById = accountPackagesById;
    }
}
