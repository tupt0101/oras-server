package capstone.oras.candidate.controller;


import capstone.oras.candidate.service.ICandidateService;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.job.service.IJobService;
import capstone.oras.jobApplication.service.IJobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/candidate-management")
public class CandidateController {

    @Autowired
    private ICandidateService candidateService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IJobApplicationService jobApplicationService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/candidate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CandidateEntity> createCandidate(@RequestBody CandidateEntity candidateEntity) {
        if (candidateEntity.getEmail() == null || candidateEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getFullname() == null || candidateEntity.getFullname().isEmpty()) {
            httpHeaders.set("error", "Fullname is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getPhoneNo() == null || candidateEntity.getPhoneNo().isEmpty()) {
            httpHeaders.set("error", "Phone Number is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getAddress() == null || candidateEntity.getAddress().isEmpty()) {
            httpHeaders.set("error", "Address is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);


    }

    @RequestMapping(value = "/candidate", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CandidateEntity> updateCandidate(@RequestBody CandidateEntity candidateEntity) {
        if (candidateEntity.getEmail() == null || candidateEntity.getEmail().isEmpty()) {
            httpHeaders.set("error", "Email is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getFullname() == null || candidateEntity.getFullname().isEmpty()) {
            httpHeaders.set("error", "Fullname is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getPhoneNo() == null || candidateEntity.getPhoneNo().isEmpty()) {
            httpHeaders.set("error", "Phone Number is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (candidateEntity.getAddress() == null || candidateEntity.getAddress().isEmpty()) {
            httpHeaders.set("error", "Address is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidates", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateEntity>> getAllCandidate() {

        return new ResponseEntity<List<CandidateEntity>>(candidateService.getAllCandidate(), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidate/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CandidateEntity> getCandidateById(@PathVariable("id") int id) {
        return new ResponseEntity<CandidateEntity>(candidateService.findCandidateById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/candidates-by-job/{jobID}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateEntity>> getAllCandidateByJobId(@PathVariable("jobID") int jobId) {
        List<JobApplicationEntity> jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobId(jobId);
        List<CandidateEntity> candidateEntityList = new ArrayList<>();
        for (int i = 0; i < jobApplicationEntityList.size(); i++) {
            candidateEntityList.add(jobApplicationEntityList.get(i).getCandidateByCandidateId());
        }

        return new ResponseEntity<List<CandidateEntity>>(candidateEntityList, HttpStatus.OK);

    }

}
