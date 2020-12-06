package capstone.oras.dao;

import capstone.oras.entity.CompanyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyRepository extends JpaRepository<CompanyEntity, Integer>, PagingAndSortingRepository<CompanyEntity, Integer> {
    List<CompanyEntity> findAllBy(Pageable pageable);
}
