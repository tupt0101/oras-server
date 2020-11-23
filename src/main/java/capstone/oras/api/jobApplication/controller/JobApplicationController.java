package capstone.oras.api.jobApplication.controller;


import capstone.oras.api.candidate.service.ICandidateService;
import capstone.oras.api.jobApplication.service.IJobApplicationService;
import capstone.oras.api.talentPool.service.ITalentPoolService;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobAccountEntity;
import capstone.oras.entity.openjob.OpenjobJobApplicationEntity;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    @Autowired
    private ITalentPoolService talentPoolService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/job-application", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> createJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {
            httpHeaders.set("error", "Apply Date is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getCandidateId() == null) {
            httpHeaders.set("error", "Candiate ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getSource() == null) {
            httpHeaders.set("error", "Source is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getTalentPoolId() == null) {
            httpHeaders.set("error", "Talent Pool ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getStatus() == null) {
            httpHeaders.set("error", "Status is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getJobId() == null) {
            httpHeaders.set("error", "Job ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {
            httpHeaders.set("error", "Job ID doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (talentPoolService.findTalentPoolEntityById(jobApplicationEntity.getTalentPoolId()) == null) {
            httpHeaders.set("error", "Talent Pool Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {
            httpHeaders.set("error", "Candidate ID doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
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
            httpHeaders.set("error", "Apply Date is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getCandidateId() == null) {
            httpHeaders.set("error", "Candiate ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getSource() == null) {
            httpHeaders.set("error", "Source is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getTalentPoolId() == null) {
            httpHeaders.set("error", "Talent Pool ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getStatus() == null) {
            httpHeaders.set("error", "Status is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobApplicationEntity.getJobId() == null) {
            httpHeaders.set("error", "Job ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {
            httpHeaders.set("error", "Job ID doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (talentPoolService.findTalentPoolEntityById(jobApplicationEntity.getTalentPoolId()) == null) {
            httpHeaders.set("error", "Talent Pool Id doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {
            httpHeaders.set("error", "Candidate ID doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);

    }


    @RequestMapping(value = "/job-application/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> getJobApplicationById(@PathVariable("id") int id) {

        return new ResponseEntity<JobApplicationEntity>(jobApplicationService.findJobApplicationById(id), HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplication() {
        List<JobApplicationEntity> lst = jobApplicationService.getAllJobApplication();
        lst.sort(Comparator.comparingInt(JobApplicationEntity::getId));
        return new ResponseEntity<List<JobApplicationEntity>>(lst, HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications-by-job-id/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationByJobId(@PathVariable("jobId") int jobId) {

        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationService.findJobApplicationsByJobId(jobId), HttpStatus.OK);

    }


    @RequestMapping(value = "/job-applications-openjob/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationMulti(@PathVariable("jobId") int jobId) {
        //get openjob token
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post company to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/job-application-management/job-application/find-by-job-id/" + jobService.getJobById(jobId).getOpenjobJobId();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        //        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<OpenjobJobApplicationEntity[]> jobApplicationsList = restTemplate.exchange(uri, HttpMethod.GET, entity, OpenjobJobApplicationEntity[].class);
        List<OpenjobJobApplicationEntity> jobApplicationEntityList = Arrays.asList(jobApplicationsList.getBody());
        List<OpenjobAccountEntity> accountEntityList = new ArrayList<>();
        List<CandidateEntity> candidateEntityList = new ArrayList<>();
        List<JobApplicationEntity> jobApplicationsOras = new ArrayList<>();
        JobEntity jobEntity = jobService.getJobById(jobId);
        int talentPoolId = jobEntity.getTalentPoolId();
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
           if (jobApplicationService.findJobApplicationsByJobIdAndCandidateId(jobId, candidateId ) == null) {
               jobApplicationEntity.setApplyDate(openjobJobApplication.getApplyAt());
//            jobApplicationEntity.setCandidateId(openjobJobApplication.getAccountId());
               jobApplicationEntity.setCv(openjobJobApplication.getCv());
               jobApplicationEntity.setJobId(jobId);
               jobApplicationEntity.setSource("openjob");
               jobApplicationEntity.setTalentPoolId(talentPoolId);
//               jobApplicationEntity.setId(openjobJobApplication.getId());
               jobApplicationEntity.setStatus("Applied");
               jobApplicationsOras.add(jobApplicationEntity);
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
