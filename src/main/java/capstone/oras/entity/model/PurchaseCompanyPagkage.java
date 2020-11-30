package capstone.oras.entity.model;

import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PurchaseCompanyPagkage implements Serializable {
    private PurchaseEntity purchaseEntity;
    private AccountPackageEntity accountPackageEntity;

    public PurchaseCompanyPagkage() {
    }
}
