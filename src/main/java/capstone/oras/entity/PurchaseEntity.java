package capstone.oras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "purchase", schema = "public", catalog = "db67ot35cl90oe")
public class PurchaseEntity {
    private int id;
    private String payerId;
    private String token;
    private Integer amount;
    private Date purchaseDate;
    private String status;
    private Integer accountId;
    private AccountEntity accountById;
    private Collection<CompanyPackageEntity> companyPackagesById;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "payer_id")
    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }


    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "amount")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "purchase_date")
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseEntity that = (PurchaseEntity) o;
        return id == that.id &&
                Objects.equals(payerId, that.payerId) &&
                Objects.equals(token, that.token) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(purchaseDate, that.purchaseDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payerId, token, amount, purchaseDate, status, accountId);
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable=false, updatable=false)
    public AccountEntity getAccountById() {
        return accountById;
    }

    public void setAccountById(AccountEntity accountById) {
        this.accountById = accountById;
    }


    @OneToMany(mappedBy = "companyById", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference(value = "purchase-company_package")
    public Collection<CompanyPackageEntity> getCompanyPackagesById() {
        return companyPackagesById;
    }

    public void setCompanyPackagesById(Collection<CompanyPackageEntity> companyPackagesById) {
        this.companyPackagesById = companyPackagesById;
    }
}
