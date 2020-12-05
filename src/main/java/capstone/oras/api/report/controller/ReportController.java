package capstone.oras.api.report.controller;

import capstone.oras.api.category.service.CategoryService;
import capstone.oras.api.currency.CurrencyService;
import capstone.oras.api.job.service.JobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.api.purchase.service.PurchaseService;
import capstone.oras.api.report.model.*;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobApplicationEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.PurchaseEntity;
import capstone.oras.entity.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static capstone.oras.common.Constant.ApplicantStatus.HIRED;
import static capstone.oras.common.Constant.JobStatus.PUBLISHED;

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

    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping(value = "/time-to-hire/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<TimeToHire>> getTimeToHire(@PathVariable("account-id") int accountId) {
        List<JobEntity> listJob = jobService.getAllJobByCreatorId(accountId);
        List<TimeToHire> timeToHires = new ArrayList<>();
        for (JobEntity jobEntity : listJob) {
            List<JobApplicationEntity> applicationEntityList = jobEntity.getJobApplicationsById().stream().filter(s -> HIRED.equals(s.getStatus())).collect(Collectors.toList());
            if (applicationEntityList.size() > 0) {
                for (JobApplicationEntity application : applicationEntityList) {
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
        return new ResponseEntity<List<TimeToHire>>(timeToHires, HttpStatus.OK);
    }

    @RequestMapping(value = "/candidate-of-job/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateOfJob>> getCandidateOfJob(@PathVariable("account-id") int accountId) {
        List<JobEntity> listJob = jobService.getAllJobByCreatorId(accountId);
        List<CandidateOfJob> candidateOfJobList = new ArrayList<>();
        for (JobEntity jobEntity : listJob) {
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
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
<<<<<<< Updated upstream
            PostByCategory postByCategory = new PostByCategory();
            postByCategory.setCategory(categoryEntity.getName());
            postByCategory.setNumOfPost(listByCatagory.size());
            postByCategories.add(postByCategory);
=======
            if (listByCatagory.size() > 0) {
                PostByCategory postByCategory = new PostByCategory();
                postByCategory.setCategory(categoryEntity.getName());
                postByCategory.setNumOfPost(listByCatagory.size());
                postByCategories.add(postByCategory);
            }
>>>>>>> Stashed changes
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
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
<<<<<<< Updated upstream
            PostByCategory postByCategory = new PostByCategory();
            postByCategory.setCategory(categoryEntity.getName());
            postByCategory.setNumOfPost(listByCatagory.size());
            postByCategories.add(postByCategory);
=======
            if (listByCatagory.size() > 0) {
                PostByCategory postByCategory = new PostByCategory();
                postByCategory.setCategory(categoryEntity.getName());
                postByCategory.setNumOfPost(listByCatagory.size());
                postByCategories.add(postByCategory);
            }
>>>>>>> Stashed changes
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
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            int totalApplication = 0;
            for (JobEntity jobEntity : listByCatagory
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
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            int totalApplication = 0;
            for (JobEntity jobEntity : listByCatagory
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

    @RequestMapping(value = "/average-salary-by-category/{base}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryByCategory(@PathVariable("base") String base) throws Exception {
        List<SalaryByCategory> salaryByCategories = getSalaryByCategories(base);
        return new ResponseEntity<List<SalaryByCategory>>(salaryByCategories, HttpStatus.OK);
    }

    private List<SalaryByCategory> getSalaryByCategories(String base) throws Exception {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<SalaryByCategory> salaryByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllJob();
        CurrencyService currencyService = new CurrencyService();
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            SalaryByCategory salaryByCategory = new SalaryByCategory();
            double totalSalary = 0;
            for (JobEntity jobEntity : listByCatagory
            ) {
                totalSalary = currencyService.currencyConverter(base, jobEntity.getCurrency(), (jobEntity.getSalaryFrom() + jobEntity.getSalaryTo()) / 2) + totalSalary;
            }
            salaryByCategory.setCategory(categoryEntity.getName());
            if (listByCatagory.size() > 0) {
                salaryByCategory.setAverageSalary(totalSalary / listByCatagory.size());
                salaryByCategories.add(salaryByCategory);
            }
        }
        return salaryByCategories;
    }

    @RequestMapping(value = "/average-salary-of-account-by-category/{account-id}/{base}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryOfAccountByCategory(@PathVariable("account-id") int accountId, @PathVariable("base") String base) throws Exception {
        List<SalaryByCategory> accountSalaryByCategories = getSalaryByCategories(accountId, base);
        return new ResponseEntity<List<SalaryByCategory>>(accountSalaryByCategories, HttpStatus.OK);
    }

    private List<SalaryByCategory> getSalaryByCategories(int accountId, String base) throws Exception {
        List<CategoryEntity> listCategory = new ArrayList<>();
        listCategory = categoryService.getAllCategory();
        List<SalaryByCategory> accountSalaryByCategories = new ArrayList<>();
        List<SalaryByCategory> systemSalaryByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getClosedAndPublishedJobByCreatorId(accountId);
        CurrencyService currencyService = new CurrencyService();
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            SalaryByCategory salaryByCategory = new SalaryByCategory();
            double totalSalary = 0;
            for (JobEntity jobEntity : listByCatagory
            ) {
                totalSalary = currencyService.currencyConverter(base, jobEntity.getCurrency(), (jobEntity.getSalaryFrom() + jobEntity.getSalaryTo()) / 2) + totalSalary;
            }
            salaryByCategory.setCategory(categoryEntity.getName());
            if (listByCatagory.size() > 0) {
                salaryByCategory.setAverageSalary(totalSalary / listByCatagory.size());
                accountSalaryByCategories.add(salaryByCategory);
            }
        }
        return accountSalaryByCategories;
    }

    @RequestMapping(value = "/job-statistic-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Statistic> getJobStatisticByCreatorId(@PathVariable("id") int id) {
        List<JobEntity> listJob = jobService.getAllJobByCreatorId(id);
        Statistic statistic = new Statistic();
        int totalApplication = 0;
        int totalHiredApplicant = 0;
        int totalPublicJob = 0;
        List<Collection<JobApplicationEntity>> listApplication = listJob.stream().map(s -> s.getJobApplicationsById()).collect(Collectors.toList());
        for (Collection<JobApplicationEntity> applications : listApplication
        ) {
            totalApplication += applications.size();
            totalHiredApplicant += applications.stream().filter(s -> s.getStatus().equals(HIRED)).collect(Collectors.toList()).size();
        }
        totalPublicJob += listJob.stream().filter(s -> s.getStatus().equals(PUBLISHED)).collect(Collectors.toList()).size();
        statistic.setTotalJob(listJob.size());
        statistic.setTotalCandidate(totalApplication);
        statistic.setTotalHiredCandidate(totalHiredApplicant);
        statistic.setTotalPublishJob(totalPublicJob);
        return new ResponseEntity<Statistic>(statistic, HttpStatus.OK);
    }

    @RequestMapping(value = "/system-user-salary-by-category/{id}/{base}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<SystemAndUserSalaryReport> getSalaryReport(@PathVariable("id") int id, @PathVariable("base") String base) throws Exception {
        SystemAndUserSalaryReport systemAndUserSalaryReport = new SystemAndUserSalaryReport();
        List<SalaryByCategory> userSalaryByCategories = new ArrayList<>();
        userSalaryByCategories = getSalaryByCategories(id, base);
        List<SalaryByCategory> salaryByCategories = new ArrayList<>();
        salaryByCategories = getSalaryByCategories(base);
        List<SalaryByCategory> systemSalaryByCategories = new ArrayList<>();
        for (SalaryByCategory salaryByCategory : userSalaryByCategories
        ) {
            systemSalaryByCategories.add(salaryByCategories.stream().filter(s -> s.getCategory().equals(salaryByCategory.getCategory())).collect(Collectors.toList()).get(0));
        }
        systemAndUserSalaryReport.setSystem(systemSalaryByCategories);
        systemAndUserSalaryReport.setUser(userSalaryByCategories);
        return new ResponseEntity<SystemAndUserSalaryReport>(systemAndUserSalaryReport, HttpStatus.OK);
    }

    @RequestMapping(value = "/account-purchase-report/{id}/{year}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PurchasePerMonth>> getAccountPurchaseReport(@PathVariable("id") int id, @PathVariable("year") int year) throws Exception {
        List<PurchasePerMonth> purchasePerMonths = new ArrayList<>();
        List<PurchaseEntity> listPurchase = purchaseService.findPurchaseEntityByAccountID(id);
        if (listPurchase != null) {
            if (listPurchase.size() > 0) {
                for (PurchaseEntity purchaseEntity: listPurchase) {
                    purchaseEntity.getPurchaseDate().getMonth();
                    System.out.println(purchaseEntity.getPurchaseDate().getMonth());
                }
            }
        }
        return new ResponseEntity<List<PurchasePerMonth>>(purchasePerMonths, HttpStatus.OK);
    }


}
