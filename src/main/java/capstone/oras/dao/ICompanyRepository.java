package capstone.oras.dao;

import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.CustomCompanyEntity;
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
public interface ICompanyRepository extends JpaRepository<CompanyEntity, Integer>, PagingAndSortingRepository<CompanyEntity, Integer> {
    @Query(value = "SELECT c.*, a.fullname, a.email as account_email, a.create_date FROM company c inner join account a ON a.company_id = c.id WHERE c.name NOT LIKE '%asd%' AND c.verified = true",
            nativeQuery = true)
    List<CustomCompanyEntity> asdb(Pageable pageable, boolean verified, String name);
    int countByVerifiedAndNameIgnoreCaseLike(boolean verified, String name);
    @Query(value = "SELECT c, a.fullname, a.email as account_email, a.create_date" +
            "FROM company c inner join account a ON a.company_id = c.id" +
            "WHERE c.name NOT LIKE '%:name%'",
            nativeQuery = true)
    List<CustomCompanyEntity> findAllByNameIgnoreCaseLike(Pageable pageable, String name);
    int countByNameIgnoreCaseLike(String name);
    Optional<List<CompanyEntity>> findCompanyEntitiesByNameEqualsAndVerifiedEquals(String name, boolean verified);
    Optional<List<CompanyEntity>> findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(Integer id, String name, boolean verified);
    @Query(value = "update company set verified = true where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer verifyCompanyPass(Integer id);
}
