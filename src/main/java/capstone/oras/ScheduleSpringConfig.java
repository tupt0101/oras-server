package capstone.oras;

import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.api.notification.service.INotificationService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.ActivityEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.NotificationEntity;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static capstone.oras.common.Constant.NotiType.APPLY;
import static capstone.oras.common.Constant.OpenJobApi.OJ_JOB;
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
    INotificationService notificationService;

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
                String uri = OJ_JOB + "/" + openjobJobId + "/close";
                CommonUtils.handleOpenJobApi(uri, HttpMethod.PUT, null, OpenjobJobEntity.class);
                ActivityEntity activityEntity = new ActivityEntity();
                activityEntity.setCreatorId(job.getCreatorId());
                activityEntity.setTime(LocalDateTime.now(TIME_ZONE));
                if (job.getExpireDate().isBefore(LocalDateTime.now(TIME_ZONE))) {
                    activityEntity.setTitle("Job Expired");
                } else if (job.getApplyTo().isBefore(LocalDateTime.now(TIME_ZONE))) {
                    activityEntity.setTitle("Job's Apply To Date Reached");
                }
                jobService.closeJob(job.getId());
                activityService.createActivity(activityEntity);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void scanJobApplication() {
        List<JobEntity> jobEntities = jobService.getAllPublishedJob();
        for (JobEntity job : jobEntities) {
            if (job.getExpireDate().isAfter(LocalDateTime.now(TIME_ZONE)) || job.getApplyTo().isAfter(LocalDateTime.now(TIME_ZONE))) {
                try {
                    jobApplicationService.createJobApplications(job.getId());
                    if (jobService.getJobById(job.getId()).getTotalApplication() != job.getTotalApplication()) {
                        NotificationEntity notificationEntity = new NotificationEntity();
                        notificationEntity.setCreateDate(LocalDateTime.now(TIME_ZONE));
                        notificationEntity.setNew(true);
                        notificationEntity.setReceiverId(job.getCreatorId());
                        notificationEntity.setTargetId(job.getId());
                        notificationEntity.setType(APPLY);
                        notificationService.createNotification(notificationEntity);
                    }
                } catch (ResponseStatusException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }
}
