package capstone.oras.api.job.service;

import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;

import java.util.List;

public interface IJobService {
    JobEntity createJob(JobEntity job);
    JobEntity updateJob(JobEntity job);
    List<JobEntity> getAllJob();
    JobEntity closeJob(int id);
    JobEntity getJobById(int id);
    boolean checkJobEntityById(int id);
    List<JobEntity> getOpenJob();
    List<JobEntity> getJobByCreatorId(int id);
    List<JobEntity> getAllJobByCreatorId(int id);
    List<CategoryEntity> getAllCategories();
    List<JobEntity> getClosedAndPublishedJobByCreatorId(int id);
    List<JobEntity> getClosedAndPublishedJob(int id);

}
