package capstone.oras.job.service;

import capstone.oras.entity.JobEntity;
import capstone.oras.job.model.JobModel;

import java.util.List;

public interface IJobService {
    JobEntity createUpdateJob(JobModel job);
    List<JobEntity> getAllJob();
    JobEntity closeJob(int id);
}
