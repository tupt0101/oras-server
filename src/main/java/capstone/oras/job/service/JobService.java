package capstone.oras.job.service;

import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.JobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository IJobRepository;

    @Override
    public JobEntity createJob(JobEntity job) {
        return IJobRepository.save(job);
    }
}
