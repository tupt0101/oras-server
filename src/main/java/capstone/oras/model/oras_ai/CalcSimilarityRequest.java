package capstone.oras.model.oras_ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalcSimilarityRequest {
    private Integer job_id;
    private String jd;

    public CalcSimilarityRequest() {
    }

    public CalcSimilarityRequest(Integer job_id, String jd) {
        this.job_id = job_id;
        this.jd = jd;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }
}
