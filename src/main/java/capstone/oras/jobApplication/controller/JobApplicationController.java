package capstone.oras.jobApplication.controller;


import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.jobApplication.service.IJobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/job-application-management")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/job-application", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> createJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {

        return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/job-application", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> updateJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {

        return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);

    }


    @RequestMapping(value = "/job-application/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> getJobApplicationById(@PathVariable("id")int id) {

        return new ResponseEntity<JobApplicationEntity>(jobApplicationService.findJobApplicationById(id), HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplication() {

        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationService.getAllJobApplication(), HttpStatus.OK);

    }
}
