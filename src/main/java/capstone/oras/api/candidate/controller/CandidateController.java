package capstone.oras.api.candidate.controller;


import capstone.oras.api.candidate.service.ICandidateService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.IJobApplicationService;
import capstone.oras.entity.CandidateEntity;
import capstone.oras.entity.JobApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
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

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        } else if (candidateEntity.getFullname() == null || candidateEntity.getFullname().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fullname is empty");
        } else if (candidateEntity.getPhoneNo() == null || candidateEntity.getPhoneNo().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is empty");
        } else if (candidateEntity.getAddress() == null || candidateEntity.getAddress().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is empty");
        }
            return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);


    }

    @RequestMapping(value = "/candidate", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CandidateEntity> updateCandidate(@RequestBody CandidateEntity candidateEntity) {
        if (candidateEntity.getEmail() == null || candidateEntity.getEmail().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        } else if (candidateEntity.getFullname() == null || candidateEntity.getFullname().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fullname is empty");
        } else if (candidateEntity.getPhoneNo() == null || candidateEntity.getPhoneNo().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is empty");
        } else if (candidateEntity.getAddress() == null || candidateEntity.getAddress().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is empty");
        }
        return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidates", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateEntity>> getAllCandidate() {
        List<CandidateEntity> lst = candidateService.getAllCandidate();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(CandidateEntity::getId));
        }
        return new ResponseEntity<List<CandidateEntity>>(lst, HttpStatus.OK);

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

    @RequestMapping(value = "/candidates-by-email/{email}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateEntity>> getAllCandidateByEmail(@PathVariable("email") String email) {

        return new ResponseEntity<List<CandidateEntity>>(candidateService.findCandidatesByEmail(email), HttpStatus.OK);

    }

}
