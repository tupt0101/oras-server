package capstone.oras.dao;

import capstone.oras.entity.BuffCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBuffCompanyRepository extends JpaRepository<BuffCompanyEntity, Integer>, PagingAndSortingRepository<BuffCompanyEntity, Integer> {

}
