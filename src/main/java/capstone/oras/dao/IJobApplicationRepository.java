package capstone.oras.dao;

import capstone.oras.entity.JobApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobApplicationRepository extends JpaRepository<JobApplicationEntity, Integer> {
}
