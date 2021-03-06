package capstone.oras.api.job.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobJobEntity;
import capstone.oras.model.custom.ListJobModel;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.JobStatus.CLOSED;
import static capstone.oras.common.Constant.JobStatus.PUBLISHED;
import static capstone.oras.common.Constant.OpenJobApi.OJ_JOB;
import static capstone.oras.common.Constant.TIME_ZONE;

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
    private IActivityService activityService;

    @Autowired
    public JobController(IJobService jobService, IActivityService activityService) {
        this.jobService = jobService;
        this.activityService = activityService;
    }


    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    public List<JobEntity> getAllJob() {
        List<JobEntity> lst = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(JobEntity::getId));
        }
        return lst;
    }

    @RequestMapping(value = "/jobs-paging", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListJobModel> getAllJobWithPaging(@RequestParam(value = "numOfElement") Integer numOfElement,
                                                     @RequestParam(value = "page") Integer page,
                                                     @RequestParam(value = "sort") String sort,
                                                     @RequestParam(value = "title") String title,
                                                     @RequestParam(value = "status") String status,
                                                     @RequestParam(value = "currency") String currency) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        return new ResponseEntity<>(jobService.getAllJobWithPaging(pageable, title, status, currency), HttpStatus.OK);
    }

    @PostMapping(value = "/job", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<JobEntity> createJob(@RequestBody JobEntity job) {
        JobEntity jobEntity = jobService.createJob(job);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        activityEntity.setTime(java.time.LocalDateTime.now(TIME_ZONE));
        activityEntity.setTitle(CommonUtils.jobActivityTitle(job.getTitle(), job.getStatus()));
        activityService.createActivity(activityEntity);
        return new ResponseEntity<>(jobEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "/job", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<JobEntity> updateJob(@RequestBody JobEntity job) {
        return new ResponseEntity<>(jobService.updateJob(job), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<JobEntity> closeJob(@PathVariable("id") int id) {
        if (jobService.getJobById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to update");
        }
        JobEntity job = jobService.getJobById(id);
        int openjobJobId = job.getOpenjobJobId();
        // post job to openjob
        String uri = OJ_JOB + "/" + openjobJobId + "/close";
        CommonUtils.handleOpenJobApi(uri, HttpMethod.PUT, null, OpenjobJobEntity.class);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        activityEntity.setTime(java.time.LocalDateTime.now(TIME_ZONE));
        activityEntity.setTitle(CommonUtils.jobActivityTitle(job.getTitle(), job.getStatus()));
        job = jobService.closeJob(id);
        activityService.createActivity(activityEntity);

        return new ResponseEntity<>(job, HttpStatus.OK);
    }


    @RequestMapping(value = "/job/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JobEntity> getJobById(@PathVariable("id") int id) {
        return new ResponseEntity<JobEntity>(jobService.getJobById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/open-jobs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<JobEntity>> getAllOpenJob() {
        return new ResponseEntity<List<JobEntity>>(jobService.getOpenJob(), HttpStatus.OK);
    }

    @RequestMapping(value = "/open-job-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<JobEntity>> getJobByCreatorId(@PathVariable("id") int id) {
        return new ResponseEntity<>(jobService.getJobByCreatorId(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/job-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<JobEntity>> getAllJobByCreatorId(@PathVariable("id") int id) {
        return new ResponseEntity<List<JobEntity>>(jobService.getAllJobByCreatorId(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/job-by-creator-id", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListJobModel> getAllJobByCreatorIdWithPaging(@RequestParam("id") int id,
                                                                   @RequestParam(value = "numOfElement") Integer numOfElement,
                                                                   @RequestParam(value = "page") Integer page,
                                                                   @RequestParam(value = "sort") String sort,
                                                                   @RequestParam(value = "title") String title,
                                                                   @RequestParam(value = "status") String status,
                                                                   @RequestParam(value = "currency") String currency) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        return new ResponseEntity<>(jobService.getAllJobByCreatorIdWithPaging(id, pageable, title, status, currency), HttpStatus.OK);
    }

    @RequestMapping(value = "/job/{id}/publish", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<JobEntity> publishJob(@PathVariable("id") int id) {
        JobEntity job = jobService.getJobById(id);
        if (job == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to publish");
        } else if (job.getStatus().equals(CLOSED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closed job cannot be reopen");
        }
        if (jobService.existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(job.getCreatorId(), job.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This job title already exists");
        }
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
        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
        openjobJobEntity.setSalaryTo(job.getSalaryTo());
        openjobJobEntity.setStatus(job.getStatus());
        openjobJobEntity.setTitle(job.getTitle());
        openjobJobEntity.setVacancies(job.getVacancies());
        // post job to openjob
        OpenjobJobEntity openJobEntity = CommonUtils.handleOpenJobApi(OJ_JOB, HttpMethod.POST, openjobJobEntity, OpenjobJobEntity.class);
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
        if (accountPackageEntity.getNumOfPost() == 0) {
            accountPackageEntity.setExpired(true);
        }
        job.setOpenjobJobId(openJobEntity.getId());
        job.setExpireDate(accountPackageEntity.getValidTo());
        job.setApplyFrom(LocalDateTime.now(TIME_ZONE));
        job.setStatus(PUBLISHED);

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        job = jobService.updateJob(job);
        activityEntity.setTime(java.time.LocalDateTime.now(TIME_ZONE));
        activityEntity.setTitle(CommonUtils.jobActivityTitle(job.getTitle(), job.getStatus()));
        activityService.createActivity(activityEntity);
        accountPackageService.updateAccountPackage(accountPackageEntity);

        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CategoryEntity>> getAllCategories() {
        return new ResponseEntity<>(jobService.getAllCategories(), HttpStatus.OK);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public Integer removeDraft(@RequestBody Integer[] ids) {
        return jobService.removeDraft(ids);
    }

//    @RequestMapping(value = "/closed-published-job-by-creator-id/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<List<JobEntity>> getAllClosedAndPublishedJobByCreatorId(@PathVariable("id") int id) {
//        return new ResponseEntity<List<JobEntity>>(jobService.getClosedAndPublishedJobByCreatorId(id), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/job/{id}/extend/{date}", method = RequestMethod.PUT)
//    @ResponseBody
//    public ResponseEntity<JobEntity> publishJob(@PathVariable("id") int id, @PathVariable("date") int date) {
//        if (jobService.getJobById(id) == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to publish");
//        }
//        JobEntity job = jobService.getJobById(id);
//        LocalDateTime expireDate = job.getExpireDate();
//        expireDate.plusDays(date);
//        job.setExpireDate(expireDate);
//        return new ResponseEntity<JobEntity>(this.jobService.updateJob(job), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<JobEntity> createJobMulti(@RequestBody JobEntity job) {
//        if (job.getCreatorId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
//        }
//        if (job.getTitle() == null || job.getTitle().isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is a required field");
//        }
//
//        if (job.getApplyTo() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is a required field");
//        }
//
//        if (job.getCreateDate() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is a required field");
//        }
//
//        if (job.getCurrency() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
//        }
//
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
//
//        job.setCreateDate(LocalDateTime.now(TIME_ZONE));
//        JobEntity jobEntity = jobService.createJob(job);
//
//        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
//
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
//        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
//        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
//        openjobJobEntity.setSalaryTo(job.getSalaryTo());
//        openjobJobEntity.setStatus(job.getStatus());
//        openjobJobEntity.setTitle(job.getTitle());
//        openjobJobEntity.setVacancies(job.getVacancies());
//
//        //get openjob token
////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
//        String token = CommonUtils.getOjToken();
//        // post job to openjob
//        String uri = OJ_JOB;
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//
//        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
//        try {
//            restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        } catch (HttpClientErrorException.Unauthorized e) {
//            CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
//            entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
//            restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        }
//        job.setOpenjobJobId(openjobJobEntity.getId());
////        jobService.updateJob(job);
//        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
//    }
//
//
//    @PutMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<JobEntity> updateJobMulti(@RequestBody JobEntity job) {
//        if (job.getCreatorId() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
//        }
//        if (job.getTitle() == null || job.getTitle().isEmpty()) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is a required field");
//        }
//
//        if (job.getApplyTo() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is a required field");
//        }
//
//        if (job.getCreateDate() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is a required field");
//        }
//
//        if (job.getCurrency() == null) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
//        }
//
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
//        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
//        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
//        openjobJobEntity.setSalaryTo(job.getSalaryTo());
//        openjobJobEntity.setStatus(job.getStatus());
//        openjobJobEntity.setTitle(job.getTitle());
//        openjobJobEntity.setVacancies(job.getVacancies());
//
//        //get openjob token
////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
//        String token = CommonUtils.getOjToken();
//        // post job to openjob
//        String uri = OJ_JOB;
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//
//        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
//        try {
//            restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        } catch (HttpClientErrorException.Unauthorized e) {
//            CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
//            entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
//            restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
//        }
//        job.setOpenjobJobId(openjobJobEntity.getId());
////        jobService.updateJob(job);
//        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
//    }
//
////    @PostMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
////    @ResponseBody
////    ResponseEntity<JobEntity> createJobMulti(@RequestBody JobEntity job) {
////        if (job.getCreatorId() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
////        }
////        if (job.getTitle() == null || job.getTitle().isEmpty()) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is a required field");
////        }
////        if (job.getApplyFrom() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is a required field");
////        }
////
////        if (job.getApplyTo() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is a required field");
////        }
////
////        if (job.getCreateDate() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is a required field");
////        }
////
////        if (job.getCurrency() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
////        }
////
////
////
////        if (jobService.getJobById(job.getId()) != null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
////        }
////
////        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
////        }
////
////        job.setCreateDate(LocalDateTime.now(TIME_ZONE));
////        JobEntity jobEntity = jobService.createJob(job);
////
////        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
////        openjobJobEntity.setApplyTo(job.getApplyTo());
////        openjobJobEntity.setAccountId(1);
////        openjobJobEntity.setCategory(job.getCategory());
////        // Get company id from openjob
////        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
////        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
////        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
////        openjobJobEntity.setCompanyId(openjobCompanyId);
////
////
////        openjobJobEntity.setCreateDate(job.getCreateDate());
////        openjobJobEntity.setCurrency(job.getCurrency());
////        openjobJobEntity.setDescription(job.getDescription());
////        openjobJobEntity.setJobType(job.getJobType());
////        openjobJobEntity.setLocation(job.getLocation());
////        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
////        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
////        openjobJobEntity.setSalaryTo(job.getSalaryTo());
////        openjobJobEntity.setStatus(job.getStatus());
////        openjobJobEntity.setTitle(job.getTitle());
////        openjobJobEntity.setVacancies(job.getVacancies());
////
////        //get openjob token
//////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
////        String token = CommonUtils.getOjToken();
////        // post job to openjob
////        String uri = OJ_JOB;
////        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setBearerAuth(token);
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
////
////
////        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
////        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
////        job.setOpenjobJobId(openjobJobEntity.getId());
//////        jobService.updateJob(job);
////        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
////    }
////
////
////    @PutMapping(value = "/job-openjob", consumes = MediaType.APPLICATION_JSON_VALUE)
////    @ResponseBody
////    ResponseEntity<JobEntity> updateJobMulti(@RequestBody JobEntity job) {
////        if (job.getCreatorId() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
////        }
////        if (job.getTitle() == null || job.getTitle().isEmpty()) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is a required field");
////        }
////        if (job.getApplyFrom() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is a required field");
////        }
////
////        if (job.getApplyTo() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is a required field");
////        }
////
////        if (job.getCreateDate() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is a required field");
////        }
////
////        if (job.getCurrency() == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
////        }
////
////
////
////        if (jobService.getJobById(job.getId()) == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
////        }
////
////        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
////
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
////        }
////
////        OpenjobJobEntity openjobJobEntity = new OpenjobJobEntity();
////        openjobJobEntity.setApplyTo(job.getApplyTo());
////        openjobJobEntity.setAccountId(1);
////        openjobJobEntity.setCategory(job.getCategory());
////        // Get company id from openjob
////        int companyId = accountService.findAccountEntityById(job.getCreatorId()).getCompanyId();
////        System.out.println(accountService.findAccountEntityById(job.getCreatorId()).toString());
////        int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
////        openjobJobEntity.setCompanyId(openjobCompanyId);
////
////        // Set Attribute
////        openjobJobEntity.setCreateDate(job.getCreateDate());
////        openjobJobEntity.setCurrency(job.getCurrency());
////        openjobJobEntity.setDescription(job.getDescription());
////        openjobJobEntity.setJobType(job.getJobType());
////        openjobJobEntity.setLocation(job.getLocation());
////        openjobJobEntity.setSalaryFrom(job.getSalaryFrom());
////        openjobJobEntity.setSalaryHidden(job.getSalaryHidden());
////        openjobJobEntity.setSalaryTo(job.getSalaryTo());
////        openjobJobEntity.setStatus(job.getStatus());
////        openjobJobEntity.setTitle(job.getTitle());
////        openjobJobEntity.setVacancies(job.getVacancies());
////
////        //get openjob token
//////        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
////        String token = CommonUtils.getOjToken();
////        // post job to openjob
////        String uri = OJ_JOB;
////        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setBearerAuth(token);
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
////
////
////        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
////        OpenjobJobEntity openJobEntity = restTemplate.postForObject(uri, entity, OpenjobJobEntity.class);
////        job.setOpenjobJobId(openjobJobEntity.getId());
//////        jobService.updateJob(job);
////        return new ResponseEntity<JobEntity>(jobService.updateJob(job), HttpStatus.OK);
////    }
}
