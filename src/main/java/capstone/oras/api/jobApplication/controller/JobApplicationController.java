package capstone.oras.api.jobApplication.controller;


import capstone.oras.api.candidate.service.ICandidateService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.IJobApplicationService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobAccountEntity;
import capstone.oras.entity.openjob.OpenjobJobApplicationEntity;
import capstone.oras.model.custom.ListJobApplicationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.ApplicantStatus.HIRED;
import static capstone.oras.common.Constant.TIME_ZONE;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(path = "/v1/job-application-management")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    @Autowired
    private ICandidateService candidateService;

    @Autowired
    private IJobService jobService;


    HttpHeaders headers = new HttpHeaders();

    @RequestMapping(value = "/job-application", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> createJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply Date is null");
        } else if (jobApplicationEntity.getCandidateId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candiate ID is null");
        } else if (jobApplicationEntity.getSource() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source is null");
        } else if (jobApplicationEntity.getStatus() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is null");
        } else if (jobApplicationEntity.getJobId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is null");
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID doesn't exist");
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate ID doesn't exist");
        } else
            return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);
    }

//    @RequestMapping(value = "/job-applications", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<List<JobApplicationEntity>> createJobApplications(@RequestBody List<JobApplicationEntity> jobApplicationsEntity) {
//
//        return new ResponseEntity<>(jobApplicationService.createJobApplications(jobApplicationsEntity), HttpStatus.OK);
//
//    }

    @RequestMapping(value = "/job-application", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> updateJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply Date is null");
        } else if (jobApplicationEntity.getCandidateId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candiate ID is null");
        } else if (jobApplicationEntity.getSource() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source is null");
        } else if (jobApplicationEntity.getStatus() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is null");
        } else if (jobApplicationEntity.getJobId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is null");
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID doesn't exist");
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate ID doesn't exist");
        }
        return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);

    }


    @RequestMapping(value = "/job-application/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> getJobApplicationById(@PathVariable("id") int id) {

        return new ResponseEntity<JobApplicationEntity>(jobApplicationService.findJobApplicationById(id), HttpStatus.OK);

    }

    @RequestMapping(value = "/job-application/hire/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> hireJobApplication(@PathVariable("id") int id) {
        JobApplicationEntity applicationEntity = jobApplicationService.findJobApplicationById(id);
        if (applicationEntity != null) {
            applicationEntity.setStatus(HIRED);
            applicationEntity.setHiredDate(LocalDateTime.now(TIME_ZONE));
            this.jobApplicationService.updateJobApplication(applicationEntity);
            return new ResponseEntity<JobApplicationEntity>(this.jobApplicationService.updateJobApplication(applicationEntity), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Cannot find this job");
        }
    }


    @RequestMapping(value = "/job-applications", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplication() {
        List<JobApplicationEntity> lst = jobApplicationService.getAllJobApplication();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(JobApplicationEntity::getId));
        }
        return new ResponseEntity<List<JobApplicationEntity>>(lst, HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications-by-job-id/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationByJobId(@PathVariable("jobId") int jobId) {
        List<JobApplicationEntity> jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobId(jobId);
        if (!CollectionUtils.isEmpty(jobApplicationEntityList)) {
            jobApplicationEntityList.sort(Comparator.comparingDouble(JobApplicationEntity::getMatchingRate).reversed());
        }
        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationEntityList, HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications-by-job-id", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<ListJobApplicationModel> getAllJobApplicationByJobId(@RequestParam(value = "jobId") int jobId,
                                                                        @RequestParam(value = "numOfElement") Integer numOfElement,
                                                                        @RequestParam(value = "page") Integer page,
                                                                        @RequestParam(value = "sort") String sort,
                                                                        @RequestParam(value = "status") String status,
                                                                        @RequestParam(value = "name") String name) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        ListJobApplicationModel model = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId, pageable, status, name);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping(value = "/job-application-rank-cv", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<ListJobApplicationModel> rankApplication(@RequestParam(value = "jobId") int jobId,
                                                               @RequestParam(value = "numOfElement") Integer numOfElement,
                                                               @RequestParam(value = "page") Integer page,
                                                               @RequestParam(value = "sort") String sort,
                                                               @RequestParam(value = "status") String status,
                                                               @RequestParam(value = "name") String name) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            jobApplicationService.calcSimilarity(jobId);
        } catch (Exception e) {
            httpStatus = HttpStatus.NOT_MODIFIED;
        }
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        ListJobApplicationModel jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId, pageable, status, name);
        return new ResponseEntity<>(jobApplicationEntityList, httpStatus);
    }


    @RequestMapping(value = "/job-applications-openjob/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationMulti(@PathVariable("jobId") int jobId) {
        //get openjob token
        String token = CommonUtils.getOjToken();
        // get job entity
        JobEntity jobEntity = jobService.getJobById(jobId);
        // get OpenjobJobId
        Integer ojId = jobEntity.getOpenjobJobId();
        if (ojId == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This job have not published yet");
        }
        // get applications
        String uri = "https://openjob-server.herokuapp.com/v1/job-application-management/job-application/find-by-job-id/" + ojId;
        RestTemplate restTemplate = new RestTemplate();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<OpenjobJobApplicationEntity[]> jobApplicationsList;
        try {
            jobApplicationsList = restTemplate.exchange(uri, HttpMethod.GET, entity, OpenjobJobApplicationEntity[].class);
        } catch (HttpClientErrorException.Unauthorized e) {
            CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
            entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
            jobApplicationsList = restTemplate.exchange(uri, HttpMethod.GET, entity, OpenjobJobApplicationEntity[].class);
        }
        if (jobApplicationsList.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No application");
        }
        OpenjobJobApplicationEntity[] jobApplicationEntityList = jobApplicationsList.getBody();
        // update TotalApplication
        if (jobApplicationEntityList.length != 0) {
            if (jobEntity.getTotalApplication() < jobApplicationEntityList.length) {
                jobEntity.setTotalApplication(jobApplicationEntityList.length);
                jobService.updateJob(jobEntity);
            }
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
                jobApplicationEntity.setStatus("Applied");
                jobApplicationsOras.add(jobApplicationEntity);
            } else if (!tempJobApplication.getApplyDate().isEqual(openjobJobApplication.getApplyAt())) {
                tempJobApplication.setApplyDate(openjobJobApplication.getApplyAt());
                tempJobApplication.setCv(openjobJobApplication.getCv());
                tempJobApplication.setMatchingRate(0.0);
                jobApplicationsOras.add(tempJobApplication);
            }
        }
        List<JobApplicationEntity> result = jobApplicationService.createJobApplications(jobApplicationsOras);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
