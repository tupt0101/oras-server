package capstone.oras.api.job.service;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.ICategoryRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.oras_ai.ProcessJdRequest;
import capstone.oras.model.oras_ai.ProcessJdResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;
import static capstone.oras.common.Constant.JobStatus.*;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository IJobRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private IAccountService accountService;

    RestTemplate restTemplate = CommonUtils.initRestTemplate();

    @Override
    public JobEntity createJob(JobEntity job) {
        jobValidation(job);
        try {
            job.setProcessedJd(this.processJd(job.getDescription()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        job.setCreateDate(LocalDateTime.now());
        job.setTotalApplication(0);
        return IJobRepository.save(job);
    }

    @Override
    public JobEntity updateJob(JobEntity job) {
        jobValidation(job);
        if (job.getId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JobId is 0");
        } else if (getJobById(job.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to update");
        }
        return IJobRepository.save(job);
    }

    @Override
    public List<JobEntity> getAllJob() {
        return IJobRepository.findAll();
    }

    @Override
    public List<JobEntity> getAllClosedAndPublishedJob() {
        return IJobRepository.findJobEntitiesByStatusNot(DRAFT);
    }

    @Override
    public List<JobEntity> getAllPublishedJobByCreatorId(int creatorId) {
        if (IJobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(creatorId, PUBLISHED).isPresent()) {
            return IJobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(creatorId, PUBLISHED).get();

        } else return null;
    }

    @Override
    public boolean existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(Integer creatorId, String title) {
        return IJobRepository.existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(creatorId, title, PUBLISHED);
    }

    @Override
    public List<JobEntity> getAllJobWithPaging(Pageable pageable, String title, String status, String currency) {
        title = StringUtils.isEmpty(title) ? "%" : "%" + title + "%";
        status = StringUtils.isEmpty(status) ? "%" : status;
        currency = StringUtils.isEmpty(currency) ? "%" : currency;
        return IJobRepository.findAllByTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(title, status, currency, pageable);
    }

    @Override
    public JobEntity closeJob(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JobId is 0");
        } else if (getJobById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to close");
        }
        JobEntity job = getJobById(id);
        job.setStatus(CLOSED);
        return IJobRepository.save(job);
    }

    @Override
    public JobEntity getJobById(int id) {
        if (IJobRepository.existsById(id)) {
            return IJobRepository.findById(id).get();
        } else throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Can not find job");
    }

    @Override
    public boolean checkJobEntityById(int id) {
        return IJobRepository.existsById(id);
    }

    @Override
    public List<JobEntity> getOpenJob() {
        List<Integer[]> lstNoApp = IJobRepository.findEntityAndTotalApplication();
        if (lstNoApp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No job found");
        }
        List<JobEntity> lstJob = IJobRepository.findAllByStatus(PUBLISHED).get();
        lstJob.sort(Comparator.comparingInt(JobEntity::getId));
        int i = 0;
        for (JobEntity job : lstJob) {
            job.setTotalApplication(lstNoApp.get(i++)[1]);
        }
        return lstJob;
    }

    @Override
    public List<JobEntity> getJobByCreatorId(int id) {
        List<Integer[]> lstNoApp = IJobRepository.findEntityAndTotalApplication(id);
        if (lstNoApp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No job found");
        }
        List<JobEntity> lstJob = IJobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(id, PUBLISHED).get();
        lstJob.sort(Comparator.comparingInt(JobEntity::getId));
        int i = 0;
        for (JobEntity job : lstJob) {
            job.setTotalApplication(lstNoApp.get(i++)[1]);
        }
        return lstJob;
    }

    @Override
    public List<JobEntity> getClosedAndPublishedJobByCreatorId(int id) {
        List<String> statusToSearch = new ArrayList<>();
        statusToSearch.add(PUBLISHED);
        statusToSearch.add(CLOSED);
        if (IJobRepository.findJobEntitiesByCreatorIdEqualsAndStatusIn(id, statusToSearch).isPresent()) {
            return IJobRepository.findJobEntitiesByCreatorIdEqualsAndStatusIn(id, statusToSearch).get();

        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No job found");
    }

    @Override
    public List<JobEntity> getClosedAndPublishedJob() {
        List<String> statusToSearch = new ArrayList<>();
        statusToSearch.add(PUBLISHED);
        statusToSearch.add(CLOSED);
        if (IJobRepository.findJobEntitiesByStatusIn(statusToSearch).isPresent()) {
            return IJobRepository.findJobEntitiesByStatusIn(statusToSearch).get();
        }
        return new ArrayList<>();
    }

    @Override
    public List<JobEntity> getAllPublishedJob() {
        if (IJobRepository.findJobEntitiesByStatusEquals(PUBLISHED).isPresent()) {
            return IJobRepository.findJobEntitiesByStatusEquals(PUBLISHED).get();
        }
        return new ArrayList<>();
    }

    @Override
    public List<JobEntity> getAllJobByCreatorId(int id) {
        Optional<List<JobEntity>> lstJob = IJobRepository.findJobEntitiesByCreatorIdEquals(id);
        List<JobEntity> ret = new ArrayList<>();
        if (lstJob.isPresent()) {
            ret = lstJob.get();
            ret.sort(Comparator.comparingInt(JobEntity::getId));
        }
        return ret;
    }

    @Override
    public List<JobEntity> getAllJobByCreatorIdWithPaging(int id, Pageable pageable, String title, String status, String currency) {
        title = StringUtils.isEmpty(title) ? "%" : "%" + title + "%";
        status = StringUtils.isEmpty(status) ? "%" : status;
        currency = StringUtils.isEmpty(currency) ? "%" : currency;
        return IJobRepository.findJobEntitiesByCreatorIdEqualsAndTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(id, title, status, currency, pageable);
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return iCategoryRepository.findAll();
    }

    @Override
    public String processJd(String description) {
        String uri = AI_PROCESS_HOST + "/process/jd";
        // Create HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
        ProcessJdRequest jobDesc = new ProcessJdRequest();
        jobDesc.setJd(Jsoup.parse(description).text());
        HttpEntity entity = new HttpEntity(jobDesc, headers);
        // Call process
        ProcessJdResponse processedJD = restTemplate.postForEntity(uri, entity, ProcessJdResponse.class).getBody();
        return processedJD == null ? "" : processedJD.getPrc_jd();
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
        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }
    }
}
