package capstone.oras.dao;

import capstone.oras.entity.CompanyEntity;
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
    List<CompanyEntity> findAllBy(Pageable pageable);
    Optional<List<CompanyEntity>> findCompanyEntitiesByNameEqualsAndVerifiedEquals(String name, boolean verified);
    Optional<List<CompanyEntity>> findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(Integer id, String name, boolean verified);
    @Query(value = "update company set verified = true where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer verifyCompanyPass(Integer id);
}
