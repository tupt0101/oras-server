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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;
import static capstone.oras.common.Constant.ApplicantStatus.APPL;
import static capstone.oras.common.Constant.OpenJobApi.OJ_JOB_BY_ID;

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
    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private CandidateService candidateService;
    @Override
    public JobApplicationEntity createJobApplication(JobApplicationEntity jobApplicationEntity) {
        return IJobApplicationRepository.save(jobApplicationEntity);
    }

    @Override
    public void createJobApplications(int jobId) {
        // get job entity
        JobEntity jobEntity = jobService.getJobById(jobId);
        // get OpenjobJobId
        Integer ojId = jobEntity.getOpenjobJobId();
        if (ojId == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This job have not published yet");
        }
        // get applications
        String uri = OJ_JOB_BY_ID + ojId;
        OpenjobJobApplicationEntity[] jobApplicationEntityList = CommonUtils.handleOpenJobApi(uri, HttpMethod.GET, null, OpenjobJobApplicationEntity[].class);
        if (jobApplicationEntityList == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No application");
        }

        // process application
        List<JobApplicationEntity> jobApplicationsOras = new ArrayList<>();
        JobApplicationEntity jobApplicationEntity;
        for (OpenjobJobApplicationEntity openjobJobApplication : jobApplicationEntityList) {
            jobApplicationEntity = new JobApplicationEntity();
            int candidateId;
            // check to see if candidate already in system by email
            // if not create
            // get id
            List<CandidateEntity> candByEmail = candidateService.findCandidatesByEmail(openjobJobApplication.getAccountByAccountId().getEmail());
            if (CollectionUtils.isEmpty(candByEmail)) {
                OpenjobAccountEntity openjobAccountEntity = openjobJobApplication.getAccountByAccountId();
                CandidateEntity candidateEntity = new CandidateEntity();
                candidateEntity.setPhoneNo(openjobAccountEntity.getPhoneNo());
                candidateEntity.setEmail(openjobAccountEntity.getEmail());
                candidateEntity.setFullname(openjobAccountEntity.getFullname());
                candidateEntity.setAddress(openjobAccountEntity.getAddress());
                candidateId = candidateService.createCandidate(candidateEntity).getId();
            } else {
                candidateId = candByEmail.get(0).getId();
            }
            jobApplicationEntity.setCandidateId(candidateId);
            // check if application already exist(check by candidateID and jobId if exist bot
            // if not create new
            // else update
            JobApplicationEntity tempJobApplication = jobApplicationService.findJobApplicationByJobIdAndCandidateId(jobId, candidateId);
            if (tempJobApplication == null) {
                jobApplicationEntity.setApplyDate(openjobJobApplication.getApplyAt());
                jobApplicationEntity.setCv(openjobJobApplication.getCv());
                jobApplicationEntity.setJobId(jobId);
                jobApplicationEntity.setSource("openjob");
                jobApplicationEntity.setMatchingRate(0.0);
                jobApplicationEntity.setStatus(APPL);
                jobApplicationsOras.add(jobApplicationEntity);
            } else if (!tempJobApplication.getApplyDate().isEqual(openjobJobApplication.getApplyAt())) {
                tempJobApplication.setApplyDate(openjobJobApplication.getApplyAt());
                tempJobApplication.setCv(openjobJobApplication.getCv());
                tempJobApplication.setMatchingRate(0.0);
                jobApplicationsOras.add(tempJobApplication);
            }
        }
        IJobApplicationRepository.saveAll(jobApplicationsOras);
        // update TotalApplication
        int total = jobApplicationService.findJobApplicationsByJobId(jobId).size();
        if (total != jobEntity.getTotalApplication()) {
            jobEntity.setTotalApplication(total);
            jobService.updateJob(jobEntity);
        }
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
        HttpHeaders headers = CommonUtils.initHttpHeaders();
        CalcSimilarityRequest request = new CalcSimilarityRequest(id);
        HttpEntity entity = new HttpEntity(request, headers);
        // Call process
        CalcSimilarityResponse ret;
        try {
            ret = restTemplate.postForEntity(uri, entity, CalcSimilarityResponse.class).getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Disconnect to server");
        }
        return Objects.requireNonNull(ret).getMessage();
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
