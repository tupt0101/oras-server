package capstone.oras.dao;

import capstone.oras.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IActivityRepository extends JpaRepository<ActivityEntity, Integer> {
    Optional<List<ActivityEntity>> findActivityEntitiesByCreatorIdEquals(int creatorId);
}
