package capstone.oras.api.jobApplication.service;

import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IJobApplicationRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.oras_ai.CalcSimilarityRequest;
import capstone.oras.model.oras_ai.CalcSimilarityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;

@Service
public class JobApplicationService implements IJobApplicationService {

    @Autowired
    private IJobApplicationRepository IJobApplicationRepository;
    @Autowired
    private IJobRepository iJobRepository;
    RestTemplate restTemplate = CommonUtils.initRestTemplate();

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
        if (IJobApplicationRepository.findJobApplicationEntitiesByJobIdEquals(id).isPresent()) {
            return IJobApplicationRepository.findJobApplicationEntitiesByJobIdEquals(id).get();
        } else return null;
    }

    @Override
    public List<JobApplicationEntity> findJobApplicationsByJobIdAndCandidateId(int jobId, int candiateId) {
        if (IJobApplicationRepository.findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(jobId, candiateId).isPresent()) {
            return IJobApplicationRepository.findJobApplicationEntitiesByJobIdEqualsAndCandidateIdEquals(jobId, candiateId).get();
        } else return null;
    }


    @Override
    public String calcSimilarity(Integer id) {
//        if (!iJobRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job does not exist");
//        }
        String uri = AI_PROCESS_HOST + "/calc/similarity";
        // Create HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
        Optional<JobEntity> job = iJobRepository.findById(id);
        String jd = job.get().getProcessedJd();
        Integer job_id = job.get().getId();
        CalcSimilarityRequest request = new CalcSimilarityRequest(job_id, jd);
        HttpEntity entity = new HttpEntity(request, headers);
        // Call process
        try {
            CalcSimilarityResponse ret = restTemplate.postForEntity(uri, entity, CalcSimilarityResponse.class).getBody();
            return ret.getMessage();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No applications to process");
        }
    }

    @Override
    public JobApplicationEntity findJobApplicationByJobIdAndCandidateId(int jobId, int candidateId) {
        if (IJobApplicationRepository.findJobApplicationEntityByJobIdEqualsAndCandidateIdEquals(jobId, candidateId).isPresent()) {
            return IJobApplicationRepository.findJobApplicationEntityByJobIdEqualsAndCandidateIdEquals(jobId, candidateId).get();
        } else return null;
    }

    @Override
    public List<JobApplicationEntity> findJobApplicationsByJobIdWithPaging(int id, Pageable pageable, String status, String name) {
        status = StringUtils.isEmpty(status) ? "%" : status;
        name = StringUtils.isEmpty(name) ? "%" : name;
        Optional<List<JobApplicationEntity>> ret =
                IJobApplicationRepository.findJobApplicationEntitiesByJobIdEqualsAndStatusLikeAndCandidateByCandidateId_FullnameLike(
                        id, pageable, status, name);
        return ret.orElse(null);
    }
}
