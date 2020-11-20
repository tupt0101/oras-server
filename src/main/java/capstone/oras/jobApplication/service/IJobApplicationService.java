package capstone.oras.jobApplication.service;

import capstone.oras.entity.JobApplicationEntity;

import java.util.List;

public interface IJobApplicationService {
    JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity);
    List<JobApplicationEntity> createJobApplications(List<JobApplicationEntity> jobApplicationsEntity);
    JobApplicationEntity updateJobApplication(JobApplicationEntity jobApplicationEntity);
    List<JobApplicationEntity> getAllJobApplication();
    JobApplicationEntity findJobApplicationById(int id);
    List<JobApplicationEntity> findJobApplicationsByJobId(int id);
    List<JobApplicationEntity> findJobApplicationsByJobIdAndCandidateId(int jobId,int candiateId);

}
