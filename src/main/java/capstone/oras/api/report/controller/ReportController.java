package capstone.oras.api.report.controller;

import capstone.oras.api.category.service.CategoryService;
import capstone.oras.api.job.service.JobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.api.report.model.*;
import capstone.oras.entity.CategoryEntity;
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

    @Autowired
    private CategoryService categoryService;

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
            candidateOfJob.setTotalApplication(jobEntity.getJobApplicationsById().size());
            candidateOfJob.setJob(jobEntity);
            candidateOfJobList.add(candidateOfJob);
        }
        return new ResponseEntity<List<CandidateOfJob>>(candidateOfJobList, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-post-by-category", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PostByCategory>> getTotalPostByCategory() {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<PostByCategory> postByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJob();
        for (CategoryEntity categoryEntity: listCategory
             ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            PostByCategory postByCategory = new PostByCategory();
            postByCategory.setCategory(categoryEntity.getName());
            postByCategory.setNumOfPost(listByCatagory.size());
            postByCategories.add(postByCategory);
        }
        return new ResponseEntity<List<PostByCategory>>(postByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-post-of-account-by-category/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PostByCategory>> getTotalPostOfAccountByCategory(@PathVariable("account-id") int accountId) {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<PostByCategory> postByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJobByCreatorId(accountId);
        for (CategoryEntity categoryEntity: listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            PostByCategory postByCategory = new PostByCategory();
            postByCategory.setCategory(categoryEntity.getName());
            postByCategory.setNumOfPost(listByCatagory.size());
            postByCategories.add(postByCategory);
        }
        return new ResponseEntity<List<PostByCategory>>(postByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-application-by-category", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ApplicationByCategory>> getTotalApplicationByCategory() {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<ApplicationByCategory> applicationByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJob();
        for (CategoryEntity categoryEntity: listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            int totalApplication = 0;
            for (JobEntity jobEntity: listByCatagory
                 ) {
                totalApplication += jobEntity.getJobApplicationsById().size();
            }
            ApplicationByCategory applicationByCategory = new ApplicationByCategory();
            applicationByCategory.setCategory(categoryEntity.getName());
            applicationByCategory.setNumOfApplication(totalApplication);
            applicationByCategories.add(applicationByCategory);
        }
        return new ResponseEntity<List<ApplicationByCategory>>(applicationByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-application-of-account-by-category/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ApplicationByCategory>> getTotalApplicationOfAccountByCategory(@PathVariable("account-id") int accountId) {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<ApplicationByCategory> applicationByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJobByCreatorId(accountId);
        for (CategoryEntity categoryEntity: listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            int totalApplication = 0;
            for (JobEntity jobEntity: listByCatagory
            ) {
                totalApplication += jobEntity.getJobApplicationsById().size();
            }
            ApplicationByCategory applicationByCategory = new ApplicationByCategory();
            applicationByCategory.setCategory(categoryEntity.getName());
            applicationByCategory.setNumOfApplication(totalApplication);
            applicationByCategories.add(applicationByCategory);
        }
        return new ResponseEntity<List<ApplicationByCategory>>(applicationByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/average-salary-by-category", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryByCategory() {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<SalaryByCategory> salaryByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJob();
        for (CategoryEntity categoryEntity: listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            SalaryByCategory salaryByCategory = new SalaryByCategory();
            double totalSalary = 0;
            for (JobEntity jobEntity: listByCatagory
            ) {
                totalSalary = ((jobEntity.getSalaryFrom() + jobEntity.getSalaryTo()) / 2) + totalSalary;
            }
            salaryByCategory.setCategory(categoryEntity.getName());
            if (listByCatagory.size() > 0) {
                salaryByCategory.setAverageSalary(totalSalary / listByCatagory.size());
            } else {
                salaryByCategory.setAverageSalary(0);
            }
            salaryByCategories.add(salaryByCategory);
        }
        return new ResponseEntity<List<SalaryByCategory>>(salaryByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/average-salary-of-account-by-category/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryByCategory(@PathVariable("account-id") int accountId) {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<SalaryByCategory> salaryByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJobByCreatorId(accountId);
        for (CategoryEntity categoryEntity: listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            SalaryByCategory salaryByCategory = new SalaryByCategory();
            double totalSalary = 0;
            for (JobEntity jobEntity: listByCatagory
            ) {
                totalSalary = ((jobEntity.getSalaryFrom() + jobEntity.getSalaryTo()) / 2) + totalSalary;
            }
            salaryByCategory.setCategory(categoryEntity.getName());
            if (listByCatagory.size() > 0) {
                salaryByCategory.setAverageSalary(totalSalary / listByCatagory.size());
            } else {
                salaryByCategory.setAverageSalary(0);
            }
            salaryByCategories.add(salaryByCategory);
        }
        return new ResponseEntity<List<SalaryByCategory>>(salaryByCategories, HttpStatus.OK);
    }

}
