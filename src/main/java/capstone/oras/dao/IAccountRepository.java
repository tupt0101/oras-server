package capstone.oras.dao;

import capstone.oras.entity.AccountEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<AccountEntity, Integer>, PagingAndSortingRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findAccountEntitiesByEmailEquals(String email);
    Optional<AccountEntity> findAccountEntityByCompanyIdEquals(int id);
    @Query(value = "update account set fullname = :fullname, phone_no = :phoneNo where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateFullNameAndPhoneNo(int id, String fullname, String phoneNo);

    @Query(value = "update account set password = :password where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updatePassword(int id, String password);

    @Query(value = "update account a set active = true from company c where a.company_id = c.id and c.id = :companyId", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateActiveByVerifyingCompany(Integer companyId);

    List<AccountEntity> findAllByNameIgnoreCaseLikeAndStatusEqualsAndRoleEquals(Pageable pageable, String name, String status, String role);
}
