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
    List<AccountPackageEntity> findAllByAccountById_FullnameLikeAndPackageById_NameLikeAndExpiredIs(Pageable pageable, String accountById_Fullname, boolean isExpired, String packageById_Name);
    int countByAccountById_FullnameLikeAndPackageById_NameLikeAndExpiredIs(String accountById_Fullname, boolean isExpired, String packageById_Name);
    List<AccountPackageEntity> findAllByAccountById_FullnameLikeAndPackageById_NameLike(Pageable pageable, String accountById_Fullname, String packageById_Name);
    int countByAccountById_FullnameLikeAndPackageById_NameLike(String accountById_Fullname, String packageById_Name);
    List<AccountPackageEntity> findAccountPackageEntitiesByPackageId(int id);

}
