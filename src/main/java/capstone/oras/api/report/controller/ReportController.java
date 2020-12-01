package capstone.oras.api.report.controller;

import capstone.oras.api.job.service.JobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.api.report.model.CandidateOfJob;
import capstone.oras.api.report.model.TimeToHire;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static capstone.oras.common.Constant.ApplicantStatus.HIRED;
@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/report-management")
public class ReportController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/time-to-hire/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity< List<TimeToHire>> getTimeToHire(@PathVariable("account-id") int accountId) {
        List<JobEntity> listJob = jobService.getJobByCreatorId(accountId);
        List<TimeToHire> timeToHires = new ArrayList<>();
        for (JobEntity jobEntity: listJob) {
            List<JobApplicationEntity> applicationEntityList = jobEntity.getJobApplicationsById().stream().filter(s -> HIRED.equals(s.getStatus())).collect(Collectors.toList());
            if (applicationEntityList.size() > 0) {
                for (JobApplicationEntity application: applicationEntityList) {
                    TimeToHire timeToHire = new TimeToHire();
                    timeToHire.setJobTitle(jobEntity.getTitle());
                    timeToHire.setHiredCandidate(application.getCandidateByCandidateId());
                    timeToHire.setApplyDate(application.getApplyDate());
                    timeToHire.setHiredDate(application.getHiredDate());
                    timeToHire.setTimeToHired(ChronoUnit.DAYS.between(application.getApplyDate().toLocalDate(), application.getHiredDate().toLocalDate()));
                    timeToHires.add(timeToHire);
                }
            }
        }
        return new ResponseEntity< List<TimeToHire>>(timeToHires, HttpStatus.OK);
    }

    @RequestMapping(value = "/candidate-of-job/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity< List<CandidateOfJob>> getCandidateOfJob(@PathVariable("account-id") int accountId) {
        List<JobEntity> listJob = jobService.getJobByCreatorId(accountId);
        List<CandidateOfJob> candidateOfJobList = new ArrayList<>();
        for (JobEntity jobEntity: listJob) {
            List<JobApplicationEntity> hiredList = jobEntity.getJobApplicationsById().stream().filter(s -> HIRED.equals(s.getStatus())).collect(Collectors.toList());
            CandidateOfJob candidateOfJob = new CandidateOfJob();
            candidateOfJob.setHired(hiredList.size());
            candidateOfJob.setTotalApplication(listJob.size());
            candidateOfJob.setJob(jobEntity);
            candidateOfJobList.add(candidateOfJob);
        }
        return new ResponseEntity<List<CandidateOfJob>>(candidateOfJobList, HttpStatus.OK);
    }

}
