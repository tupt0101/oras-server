package capstone.oras.api.job.service;

import capstone.oras.entity.CategoryEntity;
import capstone.oras.entity.JobEntity;
import capstone.oras.model.custom.ListJobModel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IJobService {
    JobEntity createJob(JobEntity job);
    JobEntity updateJob(JobEntity job);
    List<JobEntity> getAllJob();
    JobEntity closeJob(int id);
    JobEntity getJobById(int id);
    boolean checkJobEntityById(int id);
    List<JobEntity> getOpenJob();
    List<JobEntity> getJobByCreatorId(int id);
    List<JobEntity> getAllJobByCreatorId(int id);
    List<JobEntity> getPostedJobByCreatorId(int id);
    List<CategoryEntity> getAllCategories();
    List<JobEntity> getClosedAndPublishedJobByCreatorId(int id);
    List<JobEntity> getClosedAndPublishedJob();
    List<JobEntity> getAllPublishedJob();
    ListJobModel getAllJobWithPaging(Pageable pageable, String title, String status, String currency);
    ListJobModel getAllJobByCreatorIdWithPaging(int id, Pageable pageable, String title, String status, String currency);
    List<JobEntity> getAllClosedAndPublishedJob();
    List<JobEntity> getAllPublishedJobByCreatorId(int creatorId);
    boolean existsByCreatorIdEqualsAndTitleEqualsAndStatusIs(Integer creatorId, String title);
    String processJd(String description);
    Integer removeDraft(Integer[] ids);


}
