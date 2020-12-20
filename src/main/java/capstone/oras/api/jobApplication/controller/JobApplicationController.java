package capstone.oras.api.jobApplication.controller;


import capstone.oras.api.candidate.service.ICandidateService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.IJobApplicationService;
import capstone.oras.common.CommonUtils;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.model.custom.ListJobApplicationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.ApplicantStatus.HIRED;
import static capstone.oras.common.Constant.TIME_ZONE;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(path = "/v1/job-application-management")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    @Autowired
    private ICandidateService candidateService;

    @Autowired
    private IJobService jobService;


    HttpHeaders headers = new HttpHeaders();

    @RequestMapping(value = "/job-application", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> createJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply Date is null");
        } else if (jobApplicationEntity.getCandidateId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candiate ID is null");
        } else if (jobApplicationEntity.getSource() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source is null");
        } else if (jobApplicationEntity.getStatus() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is null");
        } else if (jobApplicationEntity.getJobId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is null");
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID doesn't exist");
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate ID doesn't exist");
        } else
            return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);
    }

//    @RequestMapping(value = "/job-applications", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<List<JobApplicationEntity>> createJobApplications(@RequestBody List<JobApplicationEntity> jobApplicationsEntity) {
//
//        return new ResponseEntity<>(jobApplicationService.createJobApplications(jobApplicationsEntity), HttpStatus.OK);
//
//    }

    @RequestMapping(value = "/job-application", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> updateJobApplication(@RequestBody JobApplicationEntity jobApplicationEntity) {
        if (jobApplicationEntity.getApplyDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply Date is null");
        } else if (jobApplicationEntity.getCandidateId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candiate ID is null");
        } else if (jobApplicationEntity.getSource() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source is null");
        } else if (jobApplicationEntity.getStatus() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is null");
        } else if (jobApplicationEntity.getJobId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is null");
        } else if (jobService.getJobById(jobApplicationEntity.getJobId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID doesn't exist");
        } else if (candidateService.findCandidateById(jobApplicationEntity.getCandidateId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate ID doesn't exist");
        }
        return new ResponseEntity<>(jobApplicationService.createJobApplication(jobApplicationEntity), HttpStatus.OK);

    }


    @RequestMapping(value = "/job-application/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> getJobApplicationById(@PathVariable("id") int id) {

        return new ResponseEntity<JobApplicationEntity>(jobApplicationService.findJobApplicationById(id), HttpStatus.OK);

    }

    @RequestMapping(value = "/job-application/hire/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<JobApplicationEntity> hireJobApplication(@PathVariable("id") int id) {
        JobApplicationEntity applicationEntity = jobApplicationService.findJobApplicationById(id);
        if (applicationEntity != null) {
            applicationEntity.setStatus(HIRED);
            applicationEntity.setHiredDate(LocalDateTime.now(TIME_ZONE));
            this.jobApplicationService.updateJobApplication(applicationEntity);
            return new ResponseEntity<JobApplicationEntity>(this.jobApplicationService.updateJobApplication(applicationEntity), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Cannot find this job");
        }
    }


    @RequestMapping(value = "/job-applications", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplication() {
        List<JobApplicationEntity> lst = jobApplicationService.getAllJobApplication();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(JobApplicationEntity::getId));
        }
        return new ResponseEntity<List<JobApplicationEntity>>(lst, HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications-by-job-id/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationByJobId(@PathVariable("jobId") int jobId) {
        List<JobApplicationEntity> jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobId(jobId);
        if (!CollectionUtils.isEmpty(jobApplicationEntityList)) {
            jobApplicationEntityList.sort(Comparator.comparingDouble(JobApplicationEntity::getMatchingRate).reversed());
        }
        return new ResponseEntity<List<JobApplicationEntity>>(jobApplicationEntityList, HttpStatus.OK);

    }

    @RequestMapping(value = "/job-applications-by-job-id", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<ListJobApplicationModel> getAllJobApplicationByJobId(@RequestParam(value = "jobId") int jobId,
                                                                        @RequestParam(value = "numOfElement") Integer numOfElement,
                                                                        @RequestParam(value = "page") Integer page,
                                                                        @RequestParam(value = "sort") String sort,
                                                                        @RequestParam(value = "status") String status,
                                                                        @RequestParam(value = "name") String name) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        ListJobApplicationModel model = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId, pageable, status, name);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @RequestMapping(value = "/job-application-rank-cv", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<ListJobApplicationModel> rankApplication(@RequestParam(value = "jobId") int jobId,
                                                               @RequestParam(value = "numOfElement") Integer numOfElement,
                                                               @RequestParam(value = "page") Integer page,
                                                               @RequestParam(value = "sort") String sort,
                                                               @RequestParam(value = "status") String status,
                                                               @RequestParam(value = "name") String name) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            jobApplicationService.calcSimilarity(jobId);
        } catch (Exception e) {
            httpStatus = HttpStatus.NOT_MODIFIED;
        }
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        ListJobApplicationModel jobApplicationEntityList = jobApplicationService.findJobApplicationsByJobIdWithPaging(jobId, pageable, status, name);
        return new ResponseEntity<>(jobApplicationEntityList, httpStatus);
    }


    @RequestMapping(value = "/job-applications-openjob/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<JobApplicationEntity>> getAllJobApplicationMulti(@PathVariable("jobId") int jobId) {
        return new ResponseEntity<>(jobApplicationService.createJobApplications(jobId), HttpStatus.OK);
    }

}
