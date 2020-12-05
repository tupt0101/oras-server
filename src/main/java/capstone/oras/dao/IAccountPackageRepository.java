package capstone.oras.dao;

import capstone.oras.entity.AccountPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountPackageRepository extends JpaRepository<AccountPackageEntity, Integer> {
    Optional<AccountPackageEntity> findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(int id);
    Optional<List<AccountPackageEntity>> findAccountPackageEntitiesByAccountIdEquals(int id);
    Optional<List<AccountPackageEntity>> findAccountPackageEntitiesByExpiredFalse();

}
