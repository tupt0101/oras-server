package capstone.oras.job.controller;

import capstone.oras.entity.JobEntity;
import capstone.oras.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {
    @Autowired
    private IJobService jobService;

    @RequestMapping(value = "/job-management/job", method = RequestMethod.POST)
    @ResponseBody
    JobEntity createJob(@RequestBody JobEntity job) {
        return jobService.saveJob(job);
    }

    @RequestMapping(value = "/job-management/job", method = RequestMethod.PUT)
    @ResponseBody
    JobEntity editJob(@RequestBody JobEntity job) {
        return jobService.saveJob(job);
    }

    @RequestMapping(value = "/job-management/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    JobEntity editJob(@PathVariable("id") int id) {
        return jobService.closeJob(id);
    }

    @RequestMapping(value = "/job-management/jobs", method = RequestMethod.GET)
    @ResponseBody
    List<JobEntity> getAllJob() {
        return jobService.getAllJob();
    }

}
