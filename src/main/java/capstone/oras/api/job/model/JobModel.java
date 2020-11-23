package capstone.oras.api.job.model;

import capstone.oras.entity.JobEntity;

public class JobModel {
    private String mode;
    private JobEntity jobEntity;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public JobEntity getJobEntity() {
        return jobEntity;
    }

    public void setJobEntity(JobEntity jobEntity) {
        this.jobEntity = jobEntity;
    }

    public JobModel() {
    }

    public JobModel(String mode, JobEntity jobEntity) {
        this.mode = mode;
        this.jobEntity = jobEntity;
    }
}
