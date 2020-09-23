package capstone.oras.dao;

import capstone.oras.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJobRepository extends JpaRepository<JobEntity, Integer> {
}
