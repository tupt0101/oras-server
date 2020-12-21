package capstone.oras.api.job.service;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.ICategoryRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.custom.ListJobModel;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static capstone.oras.common.Constant.AI_PROCESS_HOST;
import static capstone.oras.common.Constant.JobStatus.*;
import static capstone.oras.common.Constant.TIME_ZONE;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private IAccountService accountService;

    @Autowired
    public JobService(IJobRepository jobRepository, IAccountService accountService) {
        this.jobRepository = jobRepository;
        this.accountService = accountService;
    }

    RestTemplate restTemplate = CommonUtils.initRestTemplate();

    @Override
    public JobEntity createJob(JobEntity job) {
        jobValidation(job);
        try {
            job.setProcessedJd(this.processJd(job.getDescription()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        job.setCreateDate(LocalDateTime.now(TIME_ZONE));
        job.setTotalApplication(0);
        return jobRepository.save(job);
    }

    @Override
    public JobEntity updateJob(JobEntity job) {
        jobValidation(job);
        if (job.getId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JobId is 0");
        } else if (getJobById(job.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to update");
        }
        return jobRepository.save(job);
    }

    @Override
    public List<JobEntity> getAllJob() {
        return jobRepository.findAll();
    }

    @Override
    public List<JobEntity> getAllClosedAndPublishedJob() {
        return jobRepository.findJobEntitiesByStatusNot(DRAFT);
    }

    @Override
    public List<JobEntity> getAllPublishedJobByCreatorId(int creatorId) {
        return jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(creatorId, PUBLISHED);
    }

    @Override
    public boolean existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(Integer creatorId, String title) {
        return jobRepository.existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(creatorId, title, PUBLISHED);
    }

    @Override
    public ListJobModel getAllJobWithPaging(Pageable pageable, String title, String status, String currency) {
        title = "%" + title + "%";
        status = StringUtils.isEmpty(status) ? "%" : status;
        currency = StringUtils.isEmpty(currency) ? "%" : currency;
        int count = jobRepository.countByTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(title, status, currency);
        List<JobEntity> data = jobRepository.findAllByTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(title, status, currency, pageable);
        return new ListJobModel(count, data);
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
        return jobRepository.save(job);
    }

    @Override
    public JobEntity getJobById(int id) {
        if (jobRepository.existsById(id)) {
            return jobRepository.findById(id).get();
        } else throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Can not find job");
    }

    @Override
    public boolean checkJobEntityById(int id) {
        return jobRepository.existsById(id);
    }

    @Override
    public List<JobEntity> getOpenJob() {
        List<JobEntity> lstJob = jobRepository.findAllByStatus(PUBLISHED);
        lstJob.sort(Comparator.comparing(JobEntity::getApplyFrom).reversed());
        return lstJob;
    }

    @Override
    public List<JobEntity> getJobByCreatorId(int id) {
        List<JobEntity> lstJob = jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusEquals(id, PUBLISHED);
        lstJob.sort(Comparator.comparing(JobEntity::getApplyFrom).reversed());
        return lstJob;
    }

    @Override
    public List<JobEntity> getClosedAndPublishedJobByCreatorId(int id) {
        List<String> statusToSearch = new ArrayList<>();
        statusToSearch.add(PUBLISHED);
        statusToSearch.add(CLOSED);
        if (jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusIn(id, statusToSearch).isPresent()) {
            return jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusIn(id, statusToSearch).get();

        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No job found");
    }

    @Override
    public List<JobEntity> getClosedAndPublishedJob() {
        List<String> statusToSearch = new ArrayList<>();
        statusToSearch.add(PUBLISHED);
        statusToSearch.add(CLOSED);
        if (jobRepository.findJobEntitiesByStatusIn(statusToSearch).isPresent()) {
            return jobRepository.findJobEntitiesByStatusIn(statusToSearch).get();
        }
        return new ArrayList<>();
    }

    @Override
    public List<JobEntity> getAllPublishedJob() {
        if (jobRepository.findJobEntitiesByStatusEquals(PUBLISHED).isPresent()) {
            return jobRepository.findJobEntitiesByStatusEquals(PUBLISHED).get();
        }
        return new ArrayList<>();
    }

    @Override
    public List<JobEntity> getAllJobByCreatorId(int id) {
        Optional<List<JobEntity>> lstJob = jobRepository.findJobEntitiesByCreatorIdEquals(id);
        List<JobEntity> ret = new ArrayList<>();
        if (lstJob.isPresent()) {
            ret = lstJob.get();
            ret.sort(Comparator.comparingInt(JobEntity::getId));
        }
        return ret;
    }

    @Override
    public List<JobEntity> getPostedJobByCreatorId(int id) {
        List<JobEntity> lstJob = jobRepository.findJobEntitiesByCreatorIdEqualsAndStatusIsNot(id, DRAFT);
        lstJob.sort(Comparator.comparingInt(JobEntity::getId));
        return lstJob;
    }

    @Override
    public ListJobModel getAllJobByCreatorIdWithPaging(int id, Pageable pageable, String title, String status, String currency) {
        title = "%" + title + "%";
        status = StringUtils.isEmpty(status) ? "%" : status;
        currency = StringUtils.isEmpty(currency) ? "%" : currency;
        List<JobEntity> data = jobRepository.findJobEntitiesByCreatorIdEqualsAndTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(id, title, status, currency, pageable);
        int count = jobRepository.countJobEntitiesByCreatorIdEqualsAndTitleIgnoreCaseLikeAndStatusLikeAndCurrencyLike(id, title, status, currency);
        return new ListJobModel(count, data);
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
        ProcessJdResponse processedJD;
        try {
            processedJD = restTemplate.postForEntity(uri, entity, ProcessJdResponse.class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
            entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
            processedJD = restTemplate.postForEntity(uri, entity, ProcessJdResponse.class).getBody();
        }
        return processedJD == null ? "" : processedJD.getPrc_jd();
    }

    @Override
    public Integer removeDraft(Integer[] ids) {
        return jobRepository.deleteByIds(ids);
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
        if (StringUtils.isEmpty(job.getDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is a required field");
        }
        if (StringUtils.isEmpty(job.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is a required field");
        }
        if (job.getSalaryHidden() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary hidden is a required field");
        }
        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }
    }
}
