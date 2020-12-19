package capstone.oras.api.bulk_insert;


import capstone.oras.api.job.service.IJobService;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.entity.JobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/bulk")
public class BulkController {
    @Autowired
    private IJobService jobService;
    @Autowired
    private IBulkService bulkService;

    static class Signup {
        public AccountEntity accountEntity;
        public CompanyEntity companyEntity;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> signup(@RequestBody List<Signup> signup) {
        int res = bulkService.signup(signup);
        return new ResponseEntity<>("Create " + res + "/" + signup.size() + " accounts", HttpStatus.OK);
    }

    @RequestMapping(value = "/create-job", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> createJob(@RequestBody List<JobEntity> jobs) {
        int res = bulkService.createJob(jobs);
        return new ResponseEntity<>("Create " + res + "/" + jobs.size() + " accounts", HttpStatus.OK);
    }

    @RequestMapping(value = "/publish-job", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> publishJob(@Param("id") List<Integer> jobId, @Param("creator") List<Integer> creatorId) {
        int res = bulkService.publishJob(jobId, creatorId);
        return new ResponseEntity<>("Published " + res + " jobs", HttpStatus.OK);
    }
}
