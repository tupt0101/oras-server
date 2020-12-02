package capstone.oras.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "account_package", schema = "public", catalog = "db67ot35cl90oe")
public class AccountPackageEntity {
    private int id;
    private Integer accountId;
    private Integer packageId;
    private Integer purchaseId;
    private LocalDateTime validTo;
    private Integer numOfPost;
    @ApiModelProperty(hidden = true)
    private AccountEntity accountById;
    @ApiModelProperty(hidden = true)
    private PackageEntity packageById;
    @ApiModelProperty(hidden = true)
    private PurchaseEntity purchaseById;
    private boolean isExpired;

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
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "package_id")
    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    @Basic
    @Column(name = "purchase_id")
    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Basic
    @Column(name = "valid_to")
    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
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
    @Column(name = "is_expired")
    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountPackageEntity that = (AccountPackageEntity) o;
        return id == that.id &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(packageId, that.packageId) &&
                Objects.equals(purchaseId, that.purchaseId) &&
                Objects.equals(validTo, that.validTo) &&
                Objects.equals(numOfPost, that.numOfPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, packageId, purchaseId, validTo, numOfPost);
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable=false, updatable=false)
    public AccountEntity getAccountById() {
        return accountById;
    }

    public void setAccountById(AccountEntity accountById) {
        this.accountById = accountById;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "package_id", referencedColumnName = "id", insertable=false, updatable=false)
    public PackageEntity getPackageById() {
        return packageById;
    }

    public void setPackageById(PackageEntity packageById) {
        this.packageById = packageById;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id", insertable=false, updatable=false)
    public PurchaseEntity getPurchaseById() {
        return purchaseById;
    }

    public void setPurchaseById(PurchaseEntity purchaseById) {
        this.purchaseById = purchaseById;
    }
}
