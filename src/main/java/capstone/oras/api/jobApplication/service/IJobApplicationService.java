package capstone.oras.api.jobApplication.service;

import capstone.oras.entity.JobApplicationEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJobApplicationService {
    JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity);
    List<JobApplicationEntity> createJobApplications(List<JobApplicationEntity> jobApplicationsEntity);
    JobApplicationEntity updateJobApplication(JobApplicationEntity jobApplicationEntity);
    List<JobApplicationEntity> getAllJobApplication();
    JobApplicationEntity findJobApplicationById(int id);
    List<JobApplicationEntity> findJobApplicationsByJobId(int id);
    List<JobApplicationEntity> findJobApplicationsByJobIdAndCandidateId(int jobId,int candiateId);
    String calcSimilarity(Integer id);
    JobApplicationEntity findJobApplicationByJobIdAndCandidateId(int jobId,int candidateId);
    List<JobApplicationEntity> findJobApplicationsByJobIdWithPaging(int id, Pageable pageable);

}
