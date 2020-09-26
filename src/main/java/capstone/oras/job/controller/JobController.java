package capstone.oras.job.controller;

import capstone.oras.entity.JobEntity;
import capstone.oras.job.model.JobModel;
import capstone.oras.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/job-management")
public class JobController {
    @Autowired
    private IJobService jobService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    List<JobEntity> getAllJob() {
        return jobService.getAllJob();
    }

    @RequestMapping(value = "/job", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    JobEntity createUpdateJob(@RequestBody JobModel job) {
        return jobService.createUpdateJob(job);
    }


    @RequestMapping(value = "/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    JobEntity editJob(@PathVariable("id") int id) {
        return jobService.closeJob(id);
    }



}
