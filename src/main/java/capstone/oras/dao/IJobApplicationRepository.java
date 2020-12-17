package capstone.oras.dao;

import capstone.oras.entity.JobApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface IJobApplicationRepository extends JpaRepository<JobApplicationEntity, Integer>, PagingAndSortingRepository<JobApplicationEntity, Integer> {
    Optional<List<JobApplicationEntity>> findJobApplicationEntitiesByJobIdEquals(int jobId);
    Optional<List<JobApplicationEntity>> findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(int jobId, int candidateId);
    JobApplicationEntity findJobApplicationEntityByJobIdEqualsAndCandidateIdEquals(int jobId, int candidateId);
    Optional<List<JobApplicationEntity>> findJobApplicationEntitiesByJobIdEquals(int jobId, Pageable pageable);
    List<JobApplicationEntity> findJobApplicationEntitiesByJobIdEqualsAndStatusLikeAndCandidateByCandidateId_FullnameIgnoreCaseLike(
            int jobId, Pageable pageable, String status, String candidateByCandidateId_Fullname);
    int countJobApplicationEntitiesByJobIdEqualsAndStatusLikeAndCandidateByCandidateId_FullnameIgnoreCaseLike(
            int jobId, String status, String candidateByCandidateId_Fullname);

}
