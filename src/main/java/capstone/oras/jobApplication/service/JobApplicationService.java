package capstone.oras.jobApplication.service;

import capstone.oras.dao.IJobApplicationRepository;
import capstone.oras.entity.JobApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService implements IJobApplicationService{

    @Autowired
    private IJobApplicationRepository IJobApplicationRepository;

    @Override
    public JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity) {
        return IJobApplicationRepository.save(jobApplicationEntity);
    }

    @Override
    public JobApplicationEntity updateJobApplication(JobApplicationEntity jobApplicationEntity) {
        return IJobApplicationRepository.save(jobApplicationEntity);
    }

    @Override
    public List<JobApplicationEntity> getAllJobApplication() {
        return IJobApplicationRepository.findAll();
    }

    @Override
    public JobApplicationEntity findJobApplicationById(int id) {
        if (IJobApplicationRepository.findById(id).isPresent()) {
            return IJobApplicationRepository.findById(id).get();
        } else return null;
    }
}
