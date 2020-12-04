package capstone.oras.dao;

import capstone.oras.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {
    Optional<List<PurchaseEntity>> findPurchaseEntitiesByAccountIdEquals(int accountId);
}
