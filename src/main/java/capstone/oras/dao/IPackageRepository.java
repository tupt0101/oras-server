package capstone.oras.dao;

import capstone.oras.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPackageRepository extends JpaRepository<PackageEntity, Integer> {
    @Query(value = "update package set active = false where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer deactivatePackage(Integer id);

    Optional<List<PackageEntity>> findPackageEntitiesByActiveTrue();
}
