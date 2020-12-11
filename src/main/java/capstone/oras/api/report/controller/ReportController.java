package capstone.oras.api.report.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.category.service.ICategoryService;
import capstone.oras.api.currency.CurrencyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.api.jobApplication.service.JobApplicationService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.api.report.model.*;
import capstone.oras.entity.*;
import capstone.oras.entity.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static capstone.oras.common.Constant.AccountRole.ADMIN;
import static capstone.oras.common.Constant.ApplicantStatus.HIRED;
import static capstone.oras.common.Constant.JobStatus.PUBLISHED;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/report-management")
public class ReportController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private IAccountPackageService accountPackageService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private CurrencyService currencyService;
    double rateUSD = 0;
    double rateVND = 0;
    double rateSGD = 0;
    double rateEUR = 0;
    double rateJPY = 0;
    double rateCNY = 0;


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
        timeToHires.sort(Comparator.comparing(TimeToHire::getHiredDate).reversed());
        return new ResponseEntity<>(timeToHires, HttpStatus.OK);
    }

    @RequestMapping(value = "/candidate-of-job/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateOfJob>> getCandidateOfJob(@PathVariable("account-id") int accountId) {
        List<JobEntity> listJob = jobService.getPostedJobByCreatorId(accountId);
        List<CandidateOfJob> candidateOfJobList = new ArrayList<>();
        for (JobEntity jobEntity : listJob) {
            List<JobApplicationEntity> hiredList = jobEntity.getJobApplicationsById().stream()
                    .filter(s -> HIRED.equals(s.getStatus())).collect(Collectors.toList());
            CandidateOfJob candidateOfJob = new CandidateOfJob();
            candidateOfJob.setHired(hiredList.size());
            candidateOfJob.setTotalApplication(jobEntity.getJobApplicationsById().size());
            candidateOfJob.setJob(jobEntity);
            candidateOfJobList.add(candidateOfJob);
        }
        return new ResponseEntity<>(candidateOfJobList, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-post-by-category", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PostByCategory>> getTotalPostByCategory() {
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<PostByCategory> postByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getAllClosedAndPublishedJob();
        for (CategoryEntity categoryEntity : listCategory
        ) {
            List<JobEntity> listByCatagory = jobEntityList.stream().filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            if (listByCatagory.size() > 0) {
                PostByCategory postByCategory = new PostByCategory();
                postByCategory.setCategory(categoryEntity.getName());
                postByCategory.setNumOfPost(listByCatagory.size());
                postByCategories.add(postByCategory);
            }
        }
        return new ResponseEntity<List<PostByCategory>>(postByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/total-post-of-account-by-category/{account-id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PostByCategory>> getTotalPostOfAccountByCategory(@PathVariable("account-id") int accountId) {
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<PostByCategory> postByCategories = new ArrayList<>();
        List<JobEntity> jobEntityList = jobService.getPostedJobByCreatorId(accountId);
        for (CategoryEntity categoryEntity : listCategory) {
            List<JobEntity> listByCatagory = jobEntityList.stream()
                    .filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            if (listByCatagory.size() > 0) {
                PostByCategory postByCategory = new PostByCategory();
                postByCategory.setCategory(categoryEntity.getName());
                postByCategory.setNumOfPost(listByCatagory.size());
                postByCategories.add(postByCategory);
            }
        }
        return new ResponseEntity<>(postByCategories, HttpStatus.OK);
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
            if (totalApplication > 0) {
                ApplicationByCategory applicationByCategory = new ApplicationByCategory();
                applicationByCategory.setCategory(categoryEntity.getName());
                applicationByCategory.setNumOfApplication(totalApplication);
                applicationByCategories.add(applicationByCategory);
            }
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
            if (totalApplication > 0) {
                ApplicationByCategory applicationByCategory = new ApplicationByCategory();
                applicationByCategory.setCategory(categoryEntity.getName());
                applicationByCategory.setNumOfApplication(totalApplication);
                applicationByCategories.add(applicationByCategory);
            }
        }
        return new ResponseEntity<List<ApplicationByCategory>>(applicationByCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/average-salary-by-category/{base}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryByCategory(@PathVariable("base") String base) throws Exception {
        List<SalaryByCategory> salaryByCategories = getSalaryByCategories(base);
        return new ResponseEntity<List<SalaryByCategory>>(salaryByCategories, HttpStatus.OK);
    }

    // FOR SYSTEM
    private List<SalaryByCategory> getSalaryByCategories(String base) throws Exception {
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<JobEntity> jobEntityList = jobService.getAllClosedAndPublishedJob();
        return this.calAvgSalaryByCategory(listCategory, jobEntityList, base);
    }

    @RequestMapping(value = "/average-salary-of-account-by-category/{account-id}/{base}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<SalaryByCategory>> getAverageSalaryOfAccountByCategory(@PathVariable("account-id") int accountId, @PathVariable("base") String base) throws Exception {
        List<SalaryByCategory> accountSalaryByCategories = getSalaryByCategories(accountId, base);
        return new ResponseEntity<List<SalaryByCategory>>(accountSalaryByCategories, HttpStatus.OK);
    }

    // FOR ACCOUNT
    private List<SalaryByCategory> getSalaryByCategories(int accountId, String base) throws Exception {
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<JobEntity> jobEntityList = jobService.getClosedAndPublishedJobByCreatorId(accountId);
        return this.calAvgSalaryByCategory(listCategory, jobEntityList, base);
    }

    private List<SalaryByCategory> calAvgSalaryByCategory(
            List<CategoryEntity> listCategory, List<JobEntity> jobEntityList, String base)
            throws Exception {
        List<SalaryByCategory> salaryByCategories = new ArrayList<>();
        for (CategoryEntity categoryEntity : listCategory
        ) {
            // FILTER JOB BY CATEGORY
            List<JobEntity> listByCatagory = jobEntityList.stream()
                    .filter(s -> categoryEntity.getName().equals(s.getCategory())).collect(Collectors.toList());
            SalaryByCategory salaryByCategory = new SalaryByCategory();
            // CALCULATE AVERAGE SALARY
            double totalSalary = 0;
            double jobAvgSal;
            double rate;
            for (JobEntity jobEntity : listByCatagory
            ) {
                // GET EXCHANGES RATE
                String currency = jobEntity.getCurrency();
                if (!currency.equalsIgnoreCase(base)) {
                    Field field = this.getClass().getDeclaredField("rate" + jobEntity.getCurrency());
                    field.setAccessible(true);
                    rate = field.getDouble(this);
                    if (rate == 0) {
                        field.setDouble(this, currencyService.getCurrencyRate(base, jobEntity.getCurrency()));
                    }
                    rate = field.getDouble(this);
                } else {
                    rate = 1;
                }
                jobAvgSal = (jobEntity.getSalaryFrom() + jobEntity.getSalaryTo()) / 2;
                totalSalary += jobAvgSal/rate;
            }
            salaryByCategory.setCategory(categoryEntity.getName());

            if (listByCatagory.size() > 0) {
                salaryByCategory.setAverageSalary((int) (totalSalary / listByCatagory.size()));
                salaryByCategories.add(salaryByCategory);
            }
        }
        return salaryByCategories;
    }

    @RequestMapping(value = "/job-statistic-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Statistic> getJobStatisticByCreatorId(@PathVariable("id") int id) {
        List<JobEntity> listJob = jobService.getPostedJobByCreatorId(id);
        Statistic statistic = new Statistic();
        int totalApplication = 0;
        int totalHiredApplicant = 0;
        int totalPublicJob = 0;
        List<Collection<JobApplicationEntity>> listApplication = listJob.stream().map(JobEntity::getJobApplicationsById).collect(Collectors.toList());
        for (Collection<JobApplicationEntity> applications : listApplication
        ) {
            totalApplication += applications.size();
            totalHiredApplicant += (int) applications.stream().filter(s -> s.getStatus().equals(HIRED)).count();
        }
        totalPublicJob += (int) listJob.stream().filter(s -> s.getStatus().equals(PUBLISHED)).count();
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
    ResponseEntity<List<ReportPerMonth>> getAccountPurchaseReport(@PathVariable("id") int id, @PathVariable("year") int year) throws Exception {
        List<ReportPerMonth> reportPerMonths = new ArrayList<>();
        List<PurchaseEntity> listPurchase = new ArrayList<>();
        listPurchase = purchaseService.findPurchaseEntityByAccountID(id);
        if (listPurchase != null) {
            if (listPurchase.size() > 0) {
                listPurchase = getPurchaseByYear(year, listPurchase);
                reportPerMonths.add(getPurchaseByMonth(Month.JANUARY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.FEBRUARY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.MARCH, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.APRIL, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.MAY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.JUNE, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.JULY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.AUGUST, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.SEPTEMBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.OCTOBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.NOVEMBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.DECEMBER, listPurchase));
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            } else {
                reportPerMonths = createEmptyReportPerMonth();
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            }
        } else {
            reportPerMonths = createEmptyReportPerMonth();
            return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
        }

    }

    private List<ReportPerMonth> createEmptyReportPerMonth() {
        List<ReportPerMonth> reportPerMonths = new ArrayList<>();
        reportPerMonths.add(new ReportPerMonth(Month.JANUARY.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.FEBRUARY.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.MARCH.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.APRIL.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.MAY.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.JUNE.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.JULY.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.AUGUST.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.SEPTEMBER.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.OCTOBER.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.NOVEMBER.toString(), 0));
        reportPerMonths.add(new ReportPerMonth(Month.DECEMBER.toString(), 0));
        return reportPerMonths;
    }

    private List<PurchaseEntity> getPurchaseByYear(int year, List<PurchaseEntity> listPurchase) {
        return listPurchase.stream().filter(s -> s.getPurchaseDate().getYear() == year).collect(Collectors.toList());
    }

    private ReportPerMonth getPurchaseByMonth(Month month, List<PurchaseEntity> listPurchase) {
        List<PurchaseEntity> temp = new ArrayList<>();
        temp = listPurchase.stream().filter(s -> s.getPurchaseDate().getMonth().equals(month)).collect(Collectors.toList());
        ReportPerMonth reportPerMonth = new ReportPerMonth();
        reportPerMonth.setMonth(month.toString());
        if (temp.size() > 0) {
            double amount = 0;
            for (PurchaseEntity purchaseEntity : temp) {
                amount += purchaseEntity.getAmount();
            }
            reportPerMonth.setAmount(amount);
        } else {
            reportPerMonth.setAmount(0);
        }
        return reportPerMonth;
    }

    @RequestMapping(value = "/package-report", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<PackageReport>> getPackageReport() {
        List<PackageReport> result = new ArrayList<>();
        List<PackageEntity> listPackage = new ArrayList<>();
        listPackage = packageService.getAllPackage();
        if (listPackage != null) {
            for (PackageEntity packageEntity : listPackage) {
                PackageReport packageReport = new PackageReport();
                packageReport.setPackageName(packageEntity.getName());
                packageReport.setNum(accountPackageService.findAccountPackageByPackageId(packageEntity.getId()).size());
                result.add(packageReport);
            }
        }
        return new ResponseEntity<List<PackageReport>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/account-report/{year}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ReportPerMonth>> getAccountReport(@PathVariable("year") int year) {
        List<ReportPerMonth> reportPerMonths = new ArrayList<>();
        List<AccountEntity> listAccount = new ArrayList<>();
        listAccount = accountService.getAllAccount();
        if (listAccount != null) {
            if (listAccount.size() > 0) {
                listAccount = getAccountByCreateYear(year, listAccount);
                reportPerMonths.add(getAccountByMonth(Month.JANUARY, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.FEBRUARY, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.MARCH, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.APRIL, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.MAY, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.JUNE, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.JULY, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.AUGUST, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.SEPTEMBER, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.OCTOBER, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.NOVEMBER, listAccount));
                reportPerMonths.add(getAccountByMonth(Month.DECEMBER, listAccount));
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            } else {
                reportPerMonths = createEmptyReportPerMonth();
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            }
        } else {
            reportPerMonths = createEmptyReportPerMonth();
            return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
        }

    }

    private List<AccountEntity> getAccountByCreateYear(int year, List<AccountEntity> list) {
        return list.stream().filter(s -> s.getCreateDate().getYear() == year).collect(Collectors.toList());
    }

    private ReportPerMonth getAccountByMonth(Month month, List<AccountEntity> listAccount) {
        List<AccountEntity> temp = new ArrayList<>();
        temp = listAccount.stream().filter(s -> s.getCreateDate().getMonth().equals(month)).collect(Collectors.toList());
        ReportPerMonth reportPerMonth = new ReportPerMonth();
        reportPerMonth.setMonth(month.toString());
        if (temp == null) {
            reportPerMonth.setAmount(0);
        } else {
            reportPerMonth.setAmount(temp.size());
        }
        return reportPerMonth;
    }

    @RequestMapping(value = "/purchase-report/{year}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ReportPerMonth>> getPurchaseReport(@PathVariable("year") int year) {
        List<ReportPerMonth> reportPerMonths = new ArrayList<>();
        List<PurchaseEntity> listPurchase = new ArrayList<>();
        listPurchase = purchaseService.getAllPurchase();
        if (listPurchase != null) {
            if (listPurchase.size() > 0) {
                listPurchase = getPurchaseByCreateYear(year, listPurchase);
                reportPerMonths.add(getPurchaseByMonth(Month.JANUARY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.FEBRUARY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.MARCH, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.APRIL, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.MAY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.JUNE, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.JULY, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.AUGUST, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.SEPTEMBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.OCTOBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.NOVEMBER, listPurchase));
                reportPerMonths.add(getPurchaseByMonth(Month.DECEMBER, listPurchase));
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            } else {
                reportPerMonths = createEmptyReportPerMonth();
                return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
            }
        } else {
            reportPerMonths = createEmptyReportPerMonth();
            return new ResponseEntity<List<ReportPerMonth>>(reportPerMonths, HttpStatus.OK);
        }

    }

    private List<PurchaseEntity> getPurchaseByCreateYear(int year, List<PurchaseEntity> list) {
        return list.stream().filter(s -> s.getPurchaseDate().getYear() == year).collect(Collectors.toList());
    }

    @RequestMapping(value = "/system-statistic", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<SystemStatistic> getPurchaseReport() {
        SystemStatistic systemStatistic = new SystemStatistic();
        systemStatistic.setTotalJobs(jobService.getClosedAndPublishedJob().size());
        systemStatistic.setOpenJobs(jobService.getAllPublishedJob().size());
        systemStatistic.setCandidate(jobApplicationService.getAllJobApplication().size());
        systemStatistic.setUser((int) accountService.getAllAccount().stream().filter(s -> !s.getRole().equals(ADMIN)).count());
        return new ResponseEntity<>(systemStatistic, HttpStatus.OK);
    }
}
