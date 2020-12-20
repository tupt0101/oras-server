package capstone.oras;

import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobJobEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static capstone.oras.common.Constant.TIME_ZONE;

@Configuration
@EnableScheduling
public class ScheduleSpringConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    IAccountPackageService accountPackageService;

    @Autowired
    IActivityService activityService;

    @Autowired
    JobApplicationService jobApplicationService;

    @Autowired
    IJobService jobService;

//    @Scheduled(fixedRate = 1000)
//    public void scheduleFixedRateTask() {
//        System.out.println(
//                "Fixed rate task - " + System.currentTimeMillis() / 1000);
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void scheduleFixedRateTassk() {
//        System.out.println(
//                "asda - " + System.currentTimeMillis() / 1000);
//    }

    //86400000 ms = 24 hours
    @Scheduled(fixedRate = 86400000)
    public void scanForExpiredAccountPackage() {
        List<AccountPackageEntity> accountPackages = accountPackageService.findAllValidAccountPackages();
        List<AccountPackageEntity> accountPackagesToUpdate = new ArrayList<>();
        if (accountPackages == null) {
            return;
        }
        for (int i = 0; i < accountPackages.size(); i++) {
            AccountPackageEntity accountPackage = accountPackages.get(i);
            if (accountPackage.getValidTo().isBefore(LocalDateTime.now(TIME_ZONE))) {
                accountPackage.setExpired(true);
                accountPackagesToUpdate.add(accountPackage);
                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setCreatorId(accountPackage.getAccountId());
                activityEntity.setTime(java.time.LocalDateTime.now(TIME_ZONE));
                activityEntity.setTitle("Current Package Expired");
                activityEntity.setJobId(null);
                activityService.createActivity(activityEntity);
            }
        }
        accountPackageService.updateAccountPackages(accountPackagesToUpdate);
    }

    @Scheduled(fixedRate = 86400000)
    public void scanForExpiredJob() {
        List<JobEntity> jobEntities = jobService.getAllPublishedJob();
        for (JobEntity job : jobEntities) {
            if (job.getExpireDate().isBefore(LocalDateTime.now(TIME_ZONE)) || job.getApplyTo().isBefore(LocalDateTime.now(TIME_ZONE))) {
                int openjobJobId = job.getOpenjobJobId();
                //get openjob token
                String token = CommonUtils.getOjToken();
                // post job to openjob
                String uri = "https://openjob-server.herokuapp.com/v1/job-management/job/" + openjobJobId + "/close";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                HttpEntity entity = new HttpEntity(headers);
                // close job on openjob
                try {
                    restTemplate.exchange(uri, HttpMethod.PUT, entity, OpenjobJobEntity.class);
                } catch (HttpClientErrorException.Unauthorized e) {
                    CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
                    entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
                    restTemplate.exchange(uri, HttpMethod.PUT, entity, OpenjobJobEntity.class);
                }
                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setCreatorId(job.getCreatorId());
                activityEntity.setTime(LocalDateTime.now(TIME_ZONE));
                if (job.getExpireDate().isBefore(LocalDateTime.now(TIME_ZONE))) {
                    activityEntity.setTitle("Job Expired");
                } else if (job.getApplyTo().isBefore(LocalDateTime.now(TIME_ZONE))) {
                    activityEntity.setTitle("Job's Apply To Date Reached");
                }
                activityEntity.setJobId(job.getId());
                jobService.closeJob(job.getId());
                activityService.createActivity(activityEntity);
            }
        }
    }
    @Scheduled(fixedRate = 86400000)
    public void scanJobApplication() {
        List<JobEntity> jobEntities = jobService.getAllPublishedJob();
        for (JobEntity job : jobEntities) {
            if (job.getExpireDate().isBefore(LocalDateTime.now(TIME_ZONE)) || job.getApplyTo().isBefore(LocalDateTime.now(TIME_ZONE))) {
                jobApplicationService.createJobApplications(job.getId());
            }
        }
    }
}
