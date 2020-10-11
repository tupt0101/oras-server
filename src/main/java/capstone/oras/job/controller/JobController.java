package capstone.oras.job.controller;

import capstone.oras.entity.JobEntity;
import capstone.oras.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/job-management")
public class JobController {
    @Autowired
    private IJobService jobService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    List<JobEntity> getAllJob() {
        return jobService.getAllJob();
    }

    @PostMapping(value = "/job",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    JobEntity createJob(@RequestBody JobEntity job) {
        return jobService.createJob(job);
    }

    @RequestMapping(value = "/job", method = RequestMethod.PUT)
    @ResponseBody
    JobEntity updateJob(@RequestBody JobEntity job) {
        return jobService.updateJob(job);
    }

    @RequestMapping(value = "/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    JobEntity editJob(@PathVariable("id") int id) {
        return jobService.closeJob(id);
    }



}
