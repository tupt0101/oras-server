package capstone.oras.dao;

import capstone.oras.entity.AccountPackageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountPackageRepository extends JpaRepository<AccountPackageEntity, Integer>, PagingAndSortingRepository<AccountPackageEntity, Integer> {
    Optional<AccountPackageEntity> findAccountPackageEntityByAccountIdEqualsAndExpiredFalse(int id);
    Optional<List<AccountPackageEntity>> findAccountPackageEntitiesByAccountIdEquals(int id);
    Optional<List<AccountPackageEntity>> findAccountPackageEntitiesByExpiredFalse();
    List<AccountPackageEntity> findAllBy(Pageable pageable);

}
