package capstone.oras.api.bulk_insert;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.IConfirmationTokenRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobCompanyEntity;
import capstone.oras.entity.openjob.OpenjobJobEntity;
import capstone.oras.model.oras_ai.ProcessJdRequest;
import capstone.oras.model.oras_ai.ProcessJdResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;
import static capstone.oras.common.Constant.JobStatus.DRAFT;
import static capstone.oras.common.Constant.JobStatus.PUBLISHED;
import static capstone.oras.common.Constant.OpenJobApi.*;
import static capstone.oras.common.Constant.TIME_ZONE;

@Service
public class BulkService implements IBulkService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    IConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    IJobRepository jobRepository;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IJobService jobService;
    @Autowired
    private IActivityService activityService;

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity entity;
    Logger logger = Logger.getLogger(BulkService.class.getName());


    @Override
    public Integer signup(List<BulkController.Signup> signups) {
        int res = 0;
        for (BulkController.Signup signup : signups) {
            try {
                this.validateSignUp(signup);
                signup = this.postToOpenJob(signup);
                saveAccountAndCompany(signup);
                res++;
            } catch (Exception e) {
                logger.warning(signup.accountEntity.getFullname() + ": creation error");
                System.out.println(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public Integer createJob(List<JobEntity> jobs) {
        int res = 0;
        for (JobEntity job : jobs) {
            try {
                JobEntity jobEntity = saveJob(job);
                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setCreatorId(job.getCreatorId());
                activityEntity.setTime(jobEntity.getCreateDate());
                activityEntity.setTitle(CommonUtils.jobActivityTitle(job.getTitle(), job.getStatus()));
                activityService.createActivity(activityEntity);
                res++;
            } catch (Exception e) {
                logger.warning(job.getTitle() + " by ID-" + job.getCreatorId() + ": creation error");
                System.out.println(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public Integer publishJob(List<Integer> jobId, List<Integer> creatorId) {
        int res = 0;
        List<JobEntity> listJob = new ArrayList<>();
        Map<Integer, List<JobEntity>> creatorJobMap = new HashMap<>();
        for (Integer creator: creatorId) {
            List<JobEntity> jobByCreator = jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(creator, DRAFT);
            listJob.addAll(jobByCreator);
            creatorJobMap.put(creator, jobByCreator);
        }
        List<JobEntity> tmpJobList;
        for (Integer job: jobId) {
            if (listJob.stream().anyMatch(j -> j.getId() == job)) {
                continue;
            }
            JobEntity jobById = jobRepository.findJobEntitiesByIdEqualsAndStatusEquals(job, DRAFT);
            listJob.add(jobById);
            tmpJobList = creatorJobMap.get(jobById.getCreatorId());
            tmpJobList.add(jobById);
            creatorJobMap.replace(jobById.getCreatorId(), tmpJobList);
        }
        String token = CommonUtils.getOjToken();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        for (Map.Entry entry: creatorJobMap.entrySet()) {
            int companyId = accountService.findAccountEntityById((Integer) entry.getKey()).getCompanyId();
            int openjobCompanyId = companyService.findCompanyById(companyId).getOpenjobCompanyId();
            for (JobEntity job: (List<JobEntity>) entry.getValue()) {
                try {
                    postJob(job, openjobCompanyId);
                    res++;
                } catch (Exception e) {
                    logger.warning(job.getId() + "-" + job.getTitle() + " by ID-" + job.getCreatorId() + ": publishing error");
                    System.out.println(e.getMessage());
                }
            }
        }
        return res;
    }

    private void postJob(JobEntity job, Integer openjobCompanyId) {
        OpenjobJobEntity openjobJobEntity;
        job.setStatus(PUBLISHED);
        openjobJobEntity = new OpenjobJobEntity();
        openjobJobEntity.setApplyTo(job.getApplyTo());
        openjobJobEntity.setAccountId(1);
        openjobJobEntity.setCategory(job.getCategory());
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
        HttpEntity<OpenjobJobEntity> entity = new HttpEntity<>(openjobJobEntity, headers);
        openjobJobEntity= restTemplate.postForObject(OJ_JOB, entity, OpenjobJobEntity.class);
        if (openjobJobEntity != null) {
            job.setOpenjobJobId(openjobJobEntity.getId());
        }
        job.setExpireDate(LocalDateTime.of(2021,1,1,0,0));
        job.setApplyFrom(LocalDateTime.now(TIME_ZONE));
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setCreatorId(job.getCreatorId());
        job = jobService.updateJob(job);
        activityEntity.setTime(java.time.LocalDateTime.now(TIME_ZONE));
        activityEntity.setTitle(CommonUtils.jobActivityTitle(job.getTitle(), job.getStatus()));
        activityService.createActivity(activityEntity);
    }

    private JobEntity saveJob(JobEntity job) {
        jobValidation(job);
        try {
            job.setProcessedJd(this.processJd(job.getDescription()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        job.setCreateDate(randomDatetimeBetween());
        job.setTotalApplication(0);
        return jobRepository.save(job);
    }

    private String processJd(String description) {
        String uri = AI_PROCESS_HOST + "/process/jd";
        // Create HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
        ProcessJdRequest jobDesc = new ProcessJdRequest();
        jobDesc.setJd(Jsoup.parse(description).text());
        entity = new HttpEntity(jobDesc, headers);
        // Call process
        ProcessJdResponse processedJD = restTemplate.postForEntity(uri, entity, ProcessJdResponse.class).getBody();
        return processedJD == null ? "" : processedJD.getPrc_jd();
    }

    private void saveAccountAndCompany(BulkController.Signup signup) {
        CompanyEntity companyEntity = companyService.createCompany(signup.companyEntity);
        signup.accountEntity.setCompanyId(companyEntity.getId());
        signup.accountEntity.setPassword(passwordEncoder.encode(signup.accountEntity.getPassword()));
        signup.accountEntity.setCreateDate(randomDatetimeBetween());
        accountRepository.save(signup.accountEntity);
    }

    private BulkController.Signup postToOpenJob(BulkController.Signup signup) {
        // post company to openjob
        String uri = OJ_COMPANY_BY_NAME + signup.companyEntity.getName();
        // check company existence
        CompanyEntity openJobEntity = CommonUtils.handleOpenJobApi(uri, HttpMethod.GET, null,CompanyEntity.class);
        if (openJobEntity == null) {
            OpenjobCompanyEntity openjobCompanyEntity = new OpenjobCompanyEntity();
            // ????? Why set 1
            openjobCompanyEntity.setAccountId(1);
            openjobCompanyEntity.setAvatar(signup.companyEntity.getAvatar());
            openjobCompanyEntity.setDescription(signup.companyEntity.getDescription());
            openjobCompanyEntity.setEmail(signup.companyEntity.getEmail());
            openjobCompanyEntity.setLocation(signup.companyEntity.getLocation());
            openjobCompanyEntity.setName(signup.companyEntity.getName());
            openjobCompanyEntity.setPhoneNo(signup.companyEntity.getPhoneNo());
            openjobCompanyEntity.setTaxCode(signup.companyEntity.getTaxCode());
            uri = OJ_COMPANY;
            HttpEntity<OpenjobCompanyEntity> httpCompanyEntity = new HttpEntity<>(openjobCompanyEntity, headers);
            // Register
            openjobCompanyEntity = restTemplate.postForObject(uri, httpCompanyEntity, OpenjobCompanyEntity.class);
            signup.companyEntity.setOpenjobCompanyId(openjobCompanyEntity != null ? openjobCompanyEntity.getId() : 0);
        } else {
            signup.companyEntity.setOpenjobCompanyId(openJobEntity.getId());
        }
        return signup;
    }

    private void validateSignUp(BulkController.Signup signup) {
        if (StringUtils.isEmpty(signup.accountEntity.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (StringUtils.isEmpty(signup.accountEntity.getFullname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        } else if (StringUtils.isEmpty(signup.accountEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        } else if (accountService.findAccountByEmail(signup.accountEntity.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountRepository.existsById(signup.accountEntity.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        }
    }

    private void jobValidation(JobEntity job) {
        if (job.getCreatorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
        }
        if (StringUtils.isEmpty(job.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is a required field");
        }
        if (job.getApplyTo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is a required field");
        }
        if (StringUtils.isEmpty(job.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is a required field");
        }
        if (job.getVacancies() == null || job.getVacancies() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vacancies must be greater than 0");
        }
        if (StringUtils.isEmpty(job.getJobType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job Type is a required field");
        }
        if (StringUtils.isEmpty(job.getCategory())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is a required field");
        }
        if (job.getSalaryFrom() == null || job.getSalaryTo() == null || job.getSalaryFrom() <= 0 || job.getSalaryTo() < job.getSalaryFrom()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary range is invalid");
        }
        if (!accountRepository.existsById(job.getCreatorId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }
    }

    private LocalDateTime randomDatetimeBetween() {
        LocalDateTime startInclusive = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime endExclusive = LocalDateTime.now(TIME_ZONE);
        long startEpochDay = startInclusive.toLocalDate().toEpochDay();
        long endEpochDay = endExclusive.toLocalDate().toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);
        LocalDate rdDate = LocalDate.ofEpochDay(randomDay);
        Random random = new Random();
        return LocalDateTime.of(rdDate,
                LocalTime.of(random.nextInt(24), random.nextInt(60),
                        random.nextInt(60), random.nextInt(999999999 + 1)));
    }
}
