package capstone.oras.job.service;

import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.JobEntity;
import capstone.oras.job.constant.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository IJobRepository;

    @Override
    public JobEntity saveJob(JobEntity job) {
        return IJobRepository.save(job);
    }

    @Override
    public List<JobEntity> getAllJob() {
        return IJobRepository.findAll();
    }

    @Override
    public JobEntity closeJob(int id) {
        JobEntity job = IJobRepository.getOne(id);
        job.setStatus(JobStatus.CLOSED.getValue());
        return IJobRepository.save(job);
    }

}
