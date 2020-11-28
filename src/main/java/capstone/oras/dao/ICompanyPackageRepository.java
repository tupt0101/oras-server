package capstone.oras.dao;

import capstone.oras.entity.AccountPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyPackageRepository extends JpaRepository<AccountPackageEntity, Integer> {
}
