package capstone.oras.jobApplication.service;

import capstone.oras.entity.JobApplicationEntity;

import java.util.List;

public interface IJobApplicationService {
    JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity);
    JobApplicationEntity updateJobApplication(JobApplicationEntity jobApplicationEntity);
    List<JobApplicationEntity> getAllJobApplication();
    JobApplicationEntity findJobApplicationById(int id);
}
