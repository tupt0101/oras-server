package capstone.oras.api.job.service;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.constant.JobStatus;
import capstone.oras.api.talentPool.service.ITalentPoolService;
import capstone.oras.dao.ICategoryRepository;
import capstone.oras.dao.IJobRepository;
import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.oras_ai.CalcSimilarityRequest;
import capstone.oras.model.oras_ai.CalcSimilarityResponse;
import capstone.oras.model.oras_ai.ProcessJdRequest;
import capstone.oras.model.oras_ai.ProcessJdResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobService implements IJobService {
    @Autowired
    private IJobRepository IJobRepository;
    @Autowired
    private ICategoryRepository iCategoryRepository;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ITalentPoolService talentPoolService;
    RestTemplate restTemplate;

    private final String AI_PROCESS_HOST = "http://127.0.0.1:5000";

    @Override
    public JobEntity createJob(JobEntity job) {
        jobValidation(job);
        if (getJobById(job.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID already exist");
        }
        job.setProcessedJd(this.processJd(job.getDescription()));
        job.setCreateDate(LocalDateTime.now());
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
    public JobEntity closeJob(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JobId is 0");
        } else if (getJobById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find job to close");
        }
        JobEntity job = IJobRepository.getOne(id);
        job.setStatus(JobStatus.CLOSED.getValue());
        return IJobRepository.save(job);
    }

    @Override
    public JobEntity getJobById(int id) {
        if (IJobRepository.existsById(id)) {
            return IJobRepository.findById(id).get();
        } else return null;
    }

    @Override
    public boolean checkJobEntityById(int id) {
        return IJobRepository.existsById(id);
    }

    @Override
    public List<JobEntity> getOpenJob() {
        if (IJobRepository.findAllByStatus(JobStatus.OPEN.getValue()).isPresent()) {
            return IJobRepository.findAllByStatus(JobStatus.OPEN.getValue()).get();
        } else return null;
    }

    @Override
    public List<JobEntity> getJobByCreatorId(int id) {
        if(IJobRepository.findJobEntitiesByCreatorIdEquals(id).isPresent()) {
            return IJobRepository.findJobEntitiesByCreatorIdEquals(id).get();
        } else return null;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return iCategoryRepository.findAll();
    }

    @Override
    public String calcSimilarity(Integer id) {
        if (!IJobRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job does not exist");
        }
        String uri = AI_PROCESS_HOST + "/calc/similarity";
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.build();
        // Entity converter
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        // Create HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
        Optional<JobEntity> job = IJobRepository.findById(id);
        String jd = job.get().getProcessedJd();
        Integer ojId = job.get().getOpenjobJobId();
        CalcSimilarityRequest request = new CalcSimilarityRequest(ojId, jd);
        HttpEntity entity = new HttpEntity(request, headers);
        // Call process
        try {
            CalcSimilarityResponse ret = restTemplate.postForEntity(uri, entity, CalcSimilarityResponse.class).getBody();
            return ret.getMessage();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No applications to process");
        }
    }

    private String processJd(String description) {
        String uri = AI_PROCESS_HOST + "/process/jd";
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder.build();
        // Entity converter
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
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

    private void jobValidation(JobEntity job){
        if (job.getCreatorId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
        }
        if (job.getTitle() == null || job.getTitle().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is empty");
        }
        if (job.getApplyFrom() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply from is empty");
        }

        if (job.getApplyTo() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply to is empty");
        }

        if (job.getCreateDate() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create Date is empty");
        }

        if (job.getCurrency() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is empty");
        }

        if (job.getTalentPoolId() == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Poll ID is empty");
        }

        if (accountService.findAccountEntityById(job.getCreatorId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is not exist");
        }

        if (talentPoolService.findTalentPoolEntityById(job.getTalentPoolId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
        }
    }
}
