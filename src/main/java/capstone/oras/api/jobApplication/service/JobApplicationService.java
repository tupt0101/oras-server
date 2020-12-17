package capstone.oras.api.jobApplication.service;

import capstone.oras.api.job.service.IJobService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IJobApplicationRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.custom.ListJobApplicationModel;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;

@Service
@Transactional
public class JobApplicationService implements IJobApplicationService {

    @Autowired
    private IJobService jobService;
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
        if (!iJobRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job does not exist");
        }
        String uri = AI_PROCESS_HOST + "/calc/similarity";
        // Create HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
        JobEntity job = iJobRepository.findById(id).get();
        String jd = job.getProcessedJd();
        if (jd == null) {
            jd = jobService.processJd(job.getDescription());
            iJobRepository.updateProcessJd(id, jd);
        }
        Integer job_id = job.getId();
        CalcSimilarityRequest request = new CalcSimilarityRequest(job_id, jd);
        HttpEntity entity = new HttpEntity(request, headers);
        // Call process
        CalcSimilarityResponse ret;
        try {
            ret = restTemplate.postForEntity(uri, entity, CalcSimilarityResponse.class).getBody();
        } catch (HttpClientErrorException.Unauthorized ex) {
            CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
            entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
            ret = restTemplate.postForEntity(uri, entity, CalcSimilarityResponse.class).getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Disconnect to server");
        }
        return ret.getMessage();
    }

    @Override
    public JobApplicationEntity findJobApplicationByJobIdAndCandidateId(int jobId, int candidateId) {
        return IJobApplicationRepository.findJobApplicationEntityByJobIdEqualsAndCandidateIdEquals(jobId, candidateId);
    }

    @Override
    public ListJobApplicationModel findJobApplicationsByJobIdWithPaging(int id, Pageable pageable, String status, String name) {
        status = StringUtils.isEmpty(status) ? "%" : status;
        name = "%" + name + "%";
        List<JobApplicationEntity> data = IJobApplicationRepository
                .findJobApplicationEntitiesByJobIdEqualsAndStatusLikeAndCandidateByCandidateId_FullnameIgnoreCaseLike(
                        id, pageable, status, name);
        int count = IJobApplicationRepository
                .countJobApplicationEntitiesByJobIdEqualsAndStatusLikeAndCandidateByCandidateId_FullnameIgnoreCaseLike(
                        id, status, name);
        return new ListJobApplicationModel(count, data);
    }
}
