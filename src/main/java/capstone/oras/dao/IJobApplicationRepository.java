package capstone.oras.dao;

import capstone.oras.entity.JobApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IJobApplicationRepository extends JpaRepository<JobApplicationEntity, Integer> {
    Optional<List<JobApplicationEntity>> findJobApplicationEntitiesByJobIdEquals(int jobId);
    Optional<List<JobApplicationEntity>> findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(int jobId, int candidateId);
    Optional<JobApplicationEntity> findJobApplicationEntityByJobIdEqualsAndCandidateIdEquals(int jobId, int candidateId);

}
