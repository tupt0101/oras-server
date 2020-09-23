package capstone.oras.job.controller;

import capstone.oras.entity.JobEntity;
import capstone.oras.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {
    @Autowired
    private IJobService jobService;

    @RequestMapping(value = "/job", method = RequestMethod.POST)
    @ResponseBody
    JobEntity createJob(@RequestBody JobEntity job) {
        return jobService.createJob(job);
    }
}
