package capstone.oras.api.job.service;

import capstone.oras.dao.ICategoryRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.api.job.constant.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository IJobRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;


    @Override
    public JobEntity createJob(JobEntity job) {
        return IJobRepository.save(job);
    }

    @Override
    public JobEntity updateJob(JobEntity job) {
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

    @Override
    public JobEntity getJobById(int id) {
        if (IJobRepository.findById(id).isPresent()) {
            return IJobRepository.findById(id).get();
        } else return null;
    }

    @Override
    public boolean checkJobEntityById(int id) {
        return IJobRepository.findById(id).isPresent();
    }

    @Override
    public List<JobEntity> getOpenJob() {
        if (IJobRepository.findAllByStatus(JobStatus.OPEN.getValue()).isPresent()) {
            return IJobRepository.findAllByStatus(JobStatus.OPEN.getValue()).get();
        } else return null;
    }

    @Override
    public List<JobEntity> getJobByCreatorId(int id) {
        if(IJobRepository.findJobEntitiesByCreatorIdEquals(id).isPresent()) {
            return IJobRepository.findJobEntitiesByCreatorIdEquals(id).get();
        } else return null;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return iCategoryRepository.findAll();
    }

}
