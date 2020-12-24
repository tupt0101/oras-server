package capstone.oras.model.oras_ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalcSimilarityRequest {
    private Integer job_id;

    public CalcSimilarityRequest() {
    }

    public CalcSimilarityRequest(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }
}
