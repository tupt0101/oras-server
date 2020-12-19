package capstone.oras.api.jobApplication.service;

import capstone.oras.api.candidate.service.CandidateService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IJobApplicationRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobAccountEntity;
import capstone.oras.entity.openjob.OpenjobJobApplicationEntity;
import capstone.oras.model.custom.ListJobApplicationModel;
import capstone.oras.model.oras_ai.CalcSimilarityRequest;
import capstone.oras.model.oras_ai.CalcSimilarityResponse;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CandidateService candidateService;
    @Override
    public JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity) {
        return IJobApplicationRepository.save(jobApplicationEntity);
    }

    @Override
    public List<JobApplicationEntity> createJobApplications(int jobId) {
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post company to openjob
        Integer ojId = jobService.getJobById(jobId).getOpenjobJobId();
        if (ojId == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This job have not published yet");
        }
        String uri = "https://openjob-server.herokuapp.com/v1/job-application-management/job-application/find-by-job-id/" + ojId;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        //        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<OpenjobJobApplicationEntity[]> jobApplicationsList = restTemplate.exchange(uri, HttpMethod.GET, entity, OpenjobJobApplicationEntity[].class);
        if (jobApplicationsList.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No application");
        }
        List<OpenjobJobApplicationEntity> jobApplicationEntityList = Arrays.asList(jobApplicationsList.getBody());
        List<JobApplicationEntity> jobApplicationsOras = new ArrayList<>();
        JobEntity jobEntity = jobService.getJobById(jobId);
        for (int i = 0; i < jobApplicationEntityList.size(); i++) {
            OpenjobJobApplicationEntity openjobJobApplication = jobApplicationEntityList.get(i);
            JobApplicationEntity jobApplicationEntity = new JobApplicationEntity();
            int candidateId;
            // check to see if candidate already in system by email
            if (candidateService.findCandidatesByEmail(openjobJobApplication.getAccountByAccountId().getEmail()) == null) {
                OpenjobAccountEntity openjobAccountEntity = openjobJobApplication.getAccountByAccountId();
                CandidateEntity candidateEntity = new CandidateEntity();
//                candidateEntity.setId(openjobAccountEntity.getId());
                candidateEntity.setPhoneNo(openjobAccountEntity.getPhoneNo());
                candidateEntity.setEmail(openjobAccountEntity.getEmail());
                candidateEntity.setFullname(openjobAccountEntity.getFullname());
                candidateEntity.setAddress(openjobAccountEntity.getAddress());
                candidateId = candidateService.createCandidate(candidateEntity).getId();
                jobApplicationEntity.setCandidateId(candidateId);
            } else {
                candidateId = candidateService.findCandidatesByEmail(openjobJobApplication.getAccountByAccountId().getEmail()).get(0).getId();
                jobApplicationEntity.setCandidateId(candidateId);

            }
            //need to check if application already exist(check by candidateID and jobId if exist bot
            JobApplicationEntity tempJobApplication = jobApplicationService.findJobApplicationByJobIdAndCandidateId(jobId, candidateId);
            if (tempJobApplication == null) {
                jobApplicationEntity.setApplyDate(openjobJobApplication.getApplyAt());
//            jobApplicationEntity.setCandidateId(openjobJobApplication.getAccountId());
                jobApplicationEntity.setCv(openjobJobApplication.getCv());
                jobApplicationEntity.setJobId(jobId);
                jobApplicationEntity.setSource("openjob");
                jobApplicationEntity.setMatchingRate(0.0);
//               jobApplicationEntity.setId(openjobJobApplication.getId());
                jobApplicationEntity.setStatus("Applied");
                jobApplicationsOras.add(jobApplicationEntity);
            } else if (!tempJobApplication.getApplyDate().isEqual(openjobJobApplication.getApplyAt())) {
                tempJobApplication.setApplyDate(openjobJobApplication.getApplyAt());
                tempJobApplication.setCv(openjobJobApplication.getCv());
                tempJobApplication.setMatchingRate(0.0);
                jobApplicationsOras.add(tempJobApplication);
            }
        }
        return IJobApplicationRepository.saveAll(jobApplicationsOras);
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
