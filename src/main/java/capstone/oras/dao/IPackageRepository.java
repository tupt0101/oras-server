package capstone.oras.dao;

import capstone.oras.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPackageRepository extends JpaRepository<PackageEntity, Integer> {
}
