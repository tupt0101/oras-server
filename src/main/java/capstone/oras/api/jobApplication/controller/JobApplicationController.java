package capstone.oras.api.jobApplication.controller;


import capstone.oras.api.candidate.service.ICandidateService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.IJobApplicationService;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobAccountEntity;
import capstone.oras.entity.openjob.OpenjobJobApplicationEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static capstone.oras.common.Constant.ApplicantStatus.HIRED;

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


    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/job-application", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> createJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply Date is null");
        } else if (jobApplicationEntity.getCandidateId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candiate ID is null");
        } else if (jobApplicationEntity.getSource() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source is null");
        }  else if (jobApplicationEntity.getStatus() == null) {

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
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationByJobId(@RequestParam(value = "jobId") int jobId, @RequestParam(value = "numOfElement") int numOfElement, @RequestParam(value = "page") int page, @RequestParam(value = "sort") String sort) {
        String sortBy = sort.substring(1);
        Pageable pageable = PageRequest.of(page-1, numOfElement, sort.startsWith("-") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        List<JobApplicationEntity> jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId,pageable);
        return new ResponseEntity<>(jobApplicationEntityList, HttpStatus.OK);
    }

    @RequestMapping(value = "/job-application-rank-cv", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> rankApplication(@RequestParam(value = "jobId") int jobId, @RequestParam(value = "numOfElement") int numOfElement, @RequestParam(value = "page") int page) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            jobApplicationService.calcSimilarity(jobId);
        } catch (Exception e) {
            httpStatus = HttpStatus.NOT_MODIFIED;
        }
        Pageable pageable = PageRequest.of(page-1, numOfElement, Sort.by("matchingRate").descending());
        List<JobApplicationEntity> jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId,pageable);
        return new ResponseEntity<>(jobApplicationEntityList, httpStatus);
    }


    @RequestMapping(value = "/job-applications-openjob/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationMulti(@PathVariable("jobId") int jobId) {
        //get openjob token
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
            if(candidateService.findCandidatesByEmail(openjobJobApplication.getAccountByAccountId().getEmail()) == null){
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
            JobApplicationEntity tempJobApplication = jobApplicationService.findJobApplicationByJobIdAndCandidateId(jobId, candidateId );
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
           } else if(!tempJobApplication.getApplyDate().isEqual(openjobJobApplication.getApplyAt()) ) {
               tempJobApplication.setApplyDate(openjobJobApplication.getApplyAt());
               tempJobApplication.setCv(openjobJobApplication.getCv());
               tempJobApplication.setMatchingRate(0.0);
               jobApplicationsOras.add(tempJobApplication);
          }


        }
//        for (int i = 0; i < accountEntityList.size(); i++) {
//            OpenjobAccountEntity openjobAccountEntity = accountEntityList.get(i);
//            CandidateEntity candidateEntity = new CandidateEntity();
//            candidateEntity.setId(openjobAccountEntity.getId());
//            candidateEntity.setPhoneNo(openjobAccountEntity.getPhoneNo());
//            candidateEntity.setEmail(openjobAccountEntity.getEmail());
//            candidateEntity.setFullname(openjobAccountEntity.getFullname());
//            candidateEntity.setAddress(openjobAccountEntity.getAddress());
//            candidateService.createCandidate(candidateEntity);
//        }


//        for (int i = 0; i < jobApplicationEntityList.size(); i++) {
//
//
//        }

        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationService.createJobApplications(jobApplicationsOras), HttpStatus.OK);


//        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationService.getAllJobApplication(), HttpStatus.OK);

    }

}
