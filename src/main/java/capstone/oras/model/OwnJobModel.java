package capstone.oras.model;

import capstone.oras.entity.JobEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnJobModel {
    private Integer noOfApplication;
    private JobEntity jobEntity;

    public OwnJobModel() {
    }

    public OwnJobModel(Integer noOfApplication, JobEntity jobEntity) {
        this.noOfApplication = noOfApplication;
        this.jobEntity = jobEntity;
    }
}
