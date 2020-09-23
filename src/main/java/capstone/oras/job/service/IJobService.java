package capstone.oras.job.service;

import capstone.oras.entity.JobEntity;

import java.util.List;

public interface IJobService {
    JobEntity saveJob(JobEntity job);
    List<JobEntity> getAllJob();
    JobEntity closeJob(int id);
}
