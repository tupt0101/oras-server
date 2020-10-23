package capstone.oras.job.controller;

import capstone.oras.account.service.IAccountService;
import capstone.oras.entity.JobEntity;
import capstone.oras.job.service.IJobService;
import capstone.oras.talentPool.service.ITalentPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/job-management")
public class JobController {
    @Autowired
    private IJobService jobService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITalentPoolService talentPoolService;


    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    List<JobEntity> getAllJob() {
        return jobService.getAllJob();
    }

    @PostMapping(value = "/job", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JobEntity> createJob(@RequestBody JobEntity job) {
        if (job.getCreatorId() == null) {
            httpHeaders.set("error", "CreatorId is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            httpHeaders.set("error", "Title is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if (job.getApplyFrom() == null) {
            httpHeaders.set("error", "Apply from is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getApplyTo() == null) {
            httpHeaders.set("error", "Apply to is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getCreateDate() == null) {
            httpHeaders.set("error", "Create Date is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getCurrency() == null) {
            httpHeaders.set("error", "Currency is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getDescription() == null) {
            httpHeaders.set("error", "Description Date is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getTalentPoolId() == null) {
            httpHeaders.set("error", "Talent Poll ID is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (jobService.getJobById(job.getId()) != null) {
            httpHeaders.set("error", "Job ID already exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
            httpHeaders.set("error", "Account is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {
            httpHeaders.set("error", "Talent Pool ID is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(jobService.createJob(job), HttpStatus.OK);
    }

    @RequestMapping(value = "/job", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> updateJob(@RequestBody JobEntity job) {
        if (job.getId() == 0) {
            httpHeaders.set("error", "JobId is 0");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if (job.getId() != 0) {
            if (jobService.getJobById(job.getId()) == null) {
                httpHeaders.set("error", "Can not find job to update");
                return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
            }
        }

        if (job.getCreatorId() == null) {
            httpHeaders.set("error", "Creator ID is null");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            httpHeaders.set("error", "Title is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if (job.getApplyFrom() == null) {
            httpHeaders.set("error", "Apply from is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getApplyTo() == null) {
            httpHeaders.set("error", "Apply to is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getCreateDate() == null) {
            httpHeaders.set("error", "Create Date is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getCurrency() == null) {
            httpHeaders.set("error", "Currency is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getDescription() == null) {
            httpHeaders.set("error", "Description Date is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (job.getTalentPoolId() == null) {
            httpHeaders.set("error", "Talent Poll Id is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (jobService.getJobById(job.getId()) == null) {
            httpHeaders.set("error", "Job ID doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
            httpHeaders.set("error", "Account is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }


        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {
            httpHeaders.set("error", "Talent Pool ID is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(jobService.updateJob(job), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> closeJob(@PathVariable("id") int id) {
        if (jobService.getJobById(id) == null) {
            httpHeaders.set("error", "Can not find job to update");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(jobService.closeJob(id), HttpStatus.OK);
    }



    @RequestMapping(value = "/job/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<JobEntity> getJobById(@PathVariable("id") int id) {
        return new ResponseEntity<JobEntity>(jobService.getJobById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/open-jobs", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getAllOpenJob() {
        return new ResponseEntity<List<JobEntity>>(jobService.getOpenJob(), HttpStatus.OK);
    }
}
