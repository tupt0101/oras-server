package capstone.oras.api.jobApplication.service;

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
    public List<JobApplicationEntity> createJobApplications(List<JobApplicationEntity> jobApplicationsEntity) {
        return IJobApplicationRepository.saveAll(jobApplicationsEntity);
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

    @Override
    public List<JobApplicationEntity> findJobApplicationsByJobId(int id) {
        if(IJobApplicationRepository.findJobApplicationEntitiesByJobIdEquals(id).isPresent()) {
            return IJobApplicationRepository.findJobApplicationEntitiesByJobIdEquals(id).get();
        } else return null;
    }

    @Override
    public List<JobApplicationEntity> findJobApplicationsByJobIdAndCandidateId(int jobId, int candiateId) {
        if(IJobApplicationRepository.findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(jobId, candiateId).isPresent()) {
            return IJobApplicationRepository.findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(jobId, candiateId).get();
        } else return null;    }
}
