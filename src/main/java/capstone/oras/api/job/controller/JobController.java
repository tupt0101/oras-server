package capstone.oras.api.job.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.talentPool.service.ITalentPoolService;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobJobEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.JobStatus.CLOSED;
import static capstone.oras.common.Constant.JobStatus.PUBLISHED;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/job-management")
public class JobController {
    @Autowired
    private IJobService jobService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IAccountPackageService accountPackageService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ITalentPoolService talentPoolService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    List<JobEntity> getAllJob() {
        List<JobEntity> lst = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(JobEntity::getId));
        }
        return lst;
    }

    @RequestMapping(value = "/jobs-paging", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getAllJobWithPaging(@RequestParam(value = "numOfElement") int numOfElement, @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page-1, numOfElement, Sort.by("id"));

        return new ResponseEntity<List<JobEntity>>(jobService.getAllJobWithPaging(pageable), HttpStatus.OK);
    }

    @PostMapping(value = "/job", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JobEntity> createJob(@RequestBody JobEntity job) {
        JobEntity jobEntity = jobService.createJob(job);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        activityEntity.setTime(java.time.LocalDateTime.now());
        activityEntity.setTitle("Create Job Draft");
        activityEntity.setJobId(jobEntity.getId());
        activityService.createActivity(activityEntity);
        return new ResponseEntity<>(jobEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/job", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> updateJob(@RequestBody JobEntity job) {
        return new ResponseEntity<>(jobService.updateJob(job), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> closeJob(@PathVariable("id") int id) {
        if (jobService.getJobById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to update");
        }
        JobEntity job = jobService.getJobById(id);
        int openjobJobId = job.getOpenjobJobId();
        //get openjob token
//        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post job to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job/" + openjobJobId + "/close";
        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);
        // close job on openjob
        restTemplate.exchange(uri, HttpMethod.PUT, entity, OpenjobJobEntity.class);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        activityEntity.setTime(java.time.LocalDateTime.now());
        activityEntity.setTitle("Close Job");
        activityEntity.setJobId(id);
        job = jobService.closeJob(id);
        activityService.createActivity(activityEntity);

        return new ResponseEntity<>(job, HttpStatus.OK);
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

    @RequestMapping(value = "/open-job-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getJobByCreatorId(@PathVariable("id") int id) {
        return new ResponseEntity<List<JobEntity>>(jobService.getJobByCreatorId(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/closed-published-job-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getAllClosedAndPublishedJobByCreatorId(@PathVariable("id") int id) {
        return new ResponseEntity<List<JobEntity>>(jobService.getClosedAndPublishedJobByCreatorId(id), HttpStatus.OK);
    }



    @RequestMapping(value = "/job-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getAllJobByCreatorId(@PathVariable("id") int id) {
        return new ResponseEntity<List<JobEntity>>(jobService.getAllJobByCreatorId(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/job-by-creator-id", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobEntity>> getAllJobByCreatorIdWithPaging(@RequestParam("id") int id,@RequestParam(value = "numOfElement") int numOfElement, @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page-1,numOfElement,Sort.by("id"));
        return new ResponseEntity<List<JobEntity>>(jobService.getAllJobByCreatorIdWithPaging(id,pageable), HttpStatus.OK);
    }


    @RequestMapping(value = "/job/{id}/extend/{date}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> publishJob(@PathVariable("id") int id, @PathVariable("date")int date) {
        if (jobService.getJobById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to publish");
        }
        JobEntity job = jobService.getJobById(id);
        LocalDateTime expireDate = job.getExpireDate();
        expireDate.plusDays(date);
        job.setExpireDate(expireDate);
        return new ResponseEntity<JobEntity>( this.jobService.updateJob(job), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/{id}/publish", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobEntity> publishJob(@PathVariable("id") int id) {
        JobEntity tempJob = jobService.getJobById(id);
        if (tempJob == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to publish");
        } else if(tempJob.getStatus().equals(CLOSED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closed job cannot be reopen");
        }
        JobEntity job = jobService.getJobById(id);
        AccountPackageEntity accountPackageEntity = accountPackageService.findAccountPackageByAccountId(job.getCreatorId());
        if (accountPackageEntity == null) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment required");
        }
        int numOfPost = accountPackageEntity.getNumOfPost();
        if (numOfPost > 0) {
            accountPackageEntity.setNumOfPost(numOfPost - 1);
        } else {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment required");
        }
        if (accountPackageEntity.getNumOfPost() == 0 ) {
            accountPackageEntity.setExpired(true);
        }
        job.setStatus(PUBLISHED);
        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
        openjobJobEntity.setApplyTo(job.getApplyTo());
        openjobJobEntity.setAccountId(1);
        openjobJobEntity.setCategory(job.getCategory());
        // Get company id from openjob
        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
        System.out.println(openjobCompanyId);
        openjobJobEntity.setCompanyId(openjobCompanyId);
        openjobJobEntity.setCreateDate(job.getCreateDate());
        openjobJobEntity.setCurrency(job.getCurrency());
        openjobJobEntity.setDescription(job.getDescription());
        openjobJobEntity.setJobType(job.getJobType());
        openjobJobEntity.setLocation(job.getLocation());
        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
        openjobJobEntity.setSalaryTo(job.getSalaryTo());
        openjobJobEntity.setStatus(job.getStatus());
        openjobJobEntity.setTitle(job.getTitle());
        openjobJobEntity.setVacancies(job.getVacancies());
        //get openjob token
//        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post job to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
        job.setOpenjobJobId(openJobEntity.getId());
        job.setExpireDate(accountPackageEntity.getValidTo());

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        job = jobService.updateJob(job);
        activityEntity.setTime(java.time.LocalDateTime.now());
        activityEntity.setTitle("Published a Job");
        activityEntity.setJobId(id);
        activityService.createActivity(activityEntity);
        accountPackageService.updateAccountPackage(accountPackageEntity);

        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @PostMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JobEntity> createJobMulti(@RequestBody JobEntity job) {
        if (job.getCreatorId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
        }
        if (job.getTitle() == null || job.getTitle().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is empty");
        }
        if (job.getApplyFrom() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is empty");
        }

        if (job.getApplyTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is empty");
        }

        if (job.getCreateDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is empty");
        }

        if (job.getCurrency() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is empty");
        }


        if (job.getTalentPoolId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Poll ID is empty");
        }

        if (jobService.getJobById(job.getId()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
        }

        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }

        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
        }
        job.setCreateDate(LocalDateTime.now());
        JobEntity jobEntity = jobService.createJob(job);

        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();

        openjobJobEntity.setApplyTo(job.getApplyTo());
        openjobJobEntity.setAccountId(1);
        openjobJobEntity.setCategory(job.getCategory());
        // Get company id from openjob
        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
        openjobJobEntity.setCompanyId(openjobCompanyId);


        openjobJobEntity.setCreateDate(job.getCreateDate());
        openjobJobEntity.setCurrency(job.getCurrency());
        openjobJobEntity.setDescription(job.getDescription());
        openjobJobEntity.setJobType(job.getJobType());
        openjobJobEntity.setLocation(job.getLocation());
        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
        openjobJobEntity.setSalaryTo(job.getSalaryTo());
        openjobJobEntity.setStatus(job.getStatus());
        openjobJobEntity.setTitle(job.getTitle());
        openjobJobEntity.setVacancies(job.getVacancies());

        //get openjob token
//        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post job to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
        job.setOpenjobJobId(openjobJobEntity.getId());
//        jobService.updateJob(job);
        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
    }


    @PutMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<JobEntity> updateJobMulti(@RequestBody JobEntity job) {
        if (job.getCreatorId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
        }
        if (job.getTitle() == null || job.getTitle().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is empty");
        }
        if (job.getApplyFrom() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is empty");
        }

        if (job.getApplyTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is empty");
        }

        if (job.getCreateDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is empty");
        }

        if (job.getCurrency() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is empty");
        }


        if (job.getTalentPoolId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Poll ID is empty");
        }

        if (jobService.getJobById(job.getId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
        }

        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }

        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
        }

        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
        openjobJobEntity.setApplyTo(job.getApplyTo());
        openjobJobEntity.setAccountId(1);
        openjobJobEntity.setCategory(job.getCategory());
        // Get company id from openjob
        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
        openjobJobEntity.setCompanyId(openjobCompanyId);

        // Set Attribute
        openjobJobEntity.setCreateDate(job.getCreateDate());
        openjobJobEntity.setCurrency(job.getCurrency());
        openjobJobEntity.setDescription(job.getDescription());
        openjobJobEntity.setJobType(job.getJobType());
        openjobJobEntity.setLocation(job.getLocation());
        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
        openjobJobEntity.setSalaryTo(job.getSalaryTo());
        openjobJobEntity.setStatus(job.getStatus());
        openjobJobEntity.setTitle(job.getTitle());
        openjobJobEntity.setVacancies(job.getVacancies());

        //get openjob token
//        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = "Bearer " + userDetailsService.getOpenJobToken();
        // post job to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
        job.setOpenjobJobId(openjobJobEntity.getId());
//        jobService.updateJob(job);
        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
    }

//    @PostMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    ResponseEntity<JobEntity> createJobMulti(@RequestBody JobEntity job) {
//        if (job.getCreatorId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
//        }
//        if (job.getTitle() == null || job.getTitle().isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is empty");
//        }
//        if (job.getApplyFrom() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is empty");
//        }
//
//        if (job.getApplyTo() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is empty");
//        }
//
//        if (job.getCreateDate() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is empty");
//        }
//
//        if (job.getCurrency() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is empty");
//        }
//
//
//        if (job.getTalentPoolId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Poll ID is empty");
//        }
//
//        if (jobService.getJobById(job.getId()) != null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
//        }
//
//        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
//        }
//
//        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
//        }
//        job.setCreateDate(LocalDateTime.now());
//        JobEntity jobEntity = jobService.createJob(job);
//
//        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
//        openjobJobEntity.setApplyTo(job.getApplyTo());
//        openjobJobEntity.setAccountId(1);
//        openjobJobEntity.setCategory(job.getCategory());
//        // Get company id from openjob
//        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
//        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
//        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
//        openjobJobEntity.setCompanyId(openjobCompanyId);
//
//
//        openjobJobEntity.setCreateDate(job.getCreateDate());
//        openjobJobEntity.setCurrency(job.getCurrency());
//        openjobJobEntity.setDescription(job.getDescription());
//        openjobJobEntity.setJobType(job.getJobType());
//        openjobJobEntity.setLocation(job.getLocation());
//        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
//        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
//        openjobJobEntity.setSalaryTo(job.getSalaryTo());
//        openjobJobEntity.setStatus(job.getStatus());
//        openjobJobEntity.setTitle(job.getTitle());
//        openjobJobEntity.setVacancies(job.getVacancies());
//
//        //get openjob token
////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
//        String token = "Bearer " + userDetailsService.getOpenJobToken();
//        // post job to openjob
//        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//
//        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
//        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        job.setOpenjobJobId(openjobJobEntity.getId());
////        jobService.updateJob(job);
//        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
//    }
//
//
//    @PutMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    ResponseEntity<JobEntity> updateJobMulti(@RequestBody JobEntity job) {
//        if (job.getCreatorId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
//        }
//        if (job.getTitle() == null || job.getTitle().isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is empty");
//        }
//        if (job.getApplyFrom() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is empty");
//        }
//
//        if (job.getApplyTo() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is empty");
//        }
//
//        if (job.getCreateDate() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is empty");
//        }
//
//        if (job.getCurrency() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is empty");
//        }
//
//
//        if (job.getTalentPoolId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Poll ID is empty");
//        }
//
//        if (jobService.getJobById(job.getId()) == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
//        }
//
//        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
//        }
//
//        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
//        }
//
//        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
//        openjobJobEntity.setApplyTo(job.getApplyTo());
//        openjobJobEntity.setAccountId(1);
//        openjobJobEntity.setCategory(job.getCategory());
//        // Get company id from openjob
//        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
//        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
//        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
//        openjobJobEntity.setCompanyId(openjobCompanyId);
//
//        // Set Attribute
//        openjobJobEntity.setCreateDate(job.getCreateDate());
//        openjobJobEntity.setCurrency(job.getCurrency());
//        openjobJobEntity.setDescription(job.getDescription());
//        openjobJobEntity.setJobType(job.getJobType());
//        openjobJobEntity.setLocation(job.getLocation());
//        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
//        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
//        openjobJobEntity.setSalaryTo(job.getSalaryTo());
//        openjobJobEntity.setStatus(job.getStatus());
//        openjobJobEntity.setTitle(job.getTitle());
//        openjobJobEntity.setVacancies(job.getVacancies());
//
//        //get openjob token
////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
//        String token = "Bearer " + userDetailsService.getOpenJobToken();
//        // post job to openjob
//        String uri = "https://openjob-server.herokuapp.com/v1/job-management/job";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//
//        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
//        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        job.setOpenjobJobId(openjobJobEntity.getId());
////        jobService.updateJob(job);
//        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
//    }


    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CategoryEntity>> getAllCategories() {
        return new ResponseEntity<List<CategoryEntity>>(jobService.getAllCategories(), HttpStatus.OK);
    }
}
