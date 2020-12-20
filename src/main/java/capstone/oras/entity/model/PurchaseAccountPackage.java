package capstone.oras.entity.model;

import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PurchaseAccountPackage implements Serializable {
    private PurchaseEntity purchaseEntity;
    private AccountPackageEntity accountPackageEntity;

    public PurchaseAccountPackage() {
    }

    public PurchaseEntity getPurchaseEntity() {
        return purchaseEntity;
    }

    public void setPurchaseEntity(PurchaseEntity purchaseEntity) {
        this.purchaseEntity = purchaseEntity;
    }

    public AccountPackageEntity getAccountPackageEntity() {
        return accountPackageEntity;
    }

    public void setAccountPackageEntity(AccountPackageEntity accountPackageEntity) {
        this.accountPackageEntity = accountPackageEntity;
    }
}
