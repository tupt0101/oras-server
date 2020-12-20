package capstone.oras.dao;

import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<CompanyEntity, Integer>, PagingAndSortingRepository<CompanyEntity, Integer> {
    @Query(value = "SELECT a " +
            "from CompanyEntity c left join AccountEntity a " +
            "on c.id = a.companyId " +
            "where upper(c.name) like upper(:name) " +
            "and c.verified = :verified")
    List<AccountEntity> accountCompanyPagingFilter(Pageable pageable, @Param("verified") boolean verified, @Param("name") String name);
    int countByVerifiedAndNameIgnoreCaseLike(boolean verified, String name);
    @Query(value = "SELECT a " +
            "from CompanyEntity c left join AccountEntity a " +
            "on c.id = a.companyId " +
            "where upper(c.name) like upper(:name)")
    List<AccountEntity> accountCompanyPagingFilterName(Pageable pageable, @Param("name") String name);
    @Query(value = "SELECT a " +
            "from CompanyEntity c left join AccountEntity a " +
            "on c.id = a.companyId " +
            "where c.id = :id")
    AccountEntity accountCompanyById(@Param("id") int id);
    int countByNameIgnoreCaseLike(String name);
    Optional<List<CompanyEntity>> findCompanyEntitiesByNameEqualsAndVerifiedEquals(String name, boolean verified);
    Optional<List<CompanyEntity>> findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(Integer id, String name, boolean verified);
    @Query(value = "update company set verified = :status, modify_date = (CURRENT_TIMESTAMP at time zone 'UTC-7') where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateCompanyStatus(@Param("id") Integer id, @Param("status") boolean status);
}
