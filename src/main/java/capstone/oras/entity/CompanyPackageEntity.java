package capstone.oras.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "company_package", schema = "public", catalog = "db67ot35cl90oe")
public class CompanyPackageEntity {
    private int id;
    private Integer companyId;
    @ApiModelProperty(hidden = true)
    private CompanyEntity companyById;
    private Integer packageId;
    @ApiModelProperty(hidden = true)
    private PackageEntity packageById;
    private Integer purchaseId;
    @ApiModelProperty(hidden = true)
    private PurchaseEntity purchaseById;
    @ApiModelProperty(example = "2020-09-28")
    private Date validTo;

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
    @Column(name = "company_id")
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPackageEntity that = (CompanyPackageEntity) o;
        return id == that.id &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(packageId, that.packageId) &&
                Objects.equals(purchaseId, that.purchaseId) &&
                Objects.equals(validTo, that.validTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyId, packageId, purchaseId, validTo);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable=false, updatable=false)
    public CompanyEntity getCompanyById() {
        return companyById;
    }

    public void setCompanyById(CompanyEntity companyById) {
        this.companyById = companyById;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "package_id", referencedColumnName = "id", insertable=false, updatable=false)
    public PackageEntity getPackageById() {
        return packageById;
    }

    public void setPackageById(PackageEntity packageById) {
        this.packageById = packageById;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id", insertable=false, updatable=false)
    public PurchaseEntity getPurchaseById() {
        return purchaseById;
    }

    public void setPurchaseById(PurchaseEntity purchaseById) {
        this.purchaseById = purchaseById;
    }
}
