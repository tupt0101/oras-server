package capstone.oras.dao;

import capstone.oras.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {
    PurchaseEntity findPurchaseEntitiesByAccountIdEquals(int accountId);
}
