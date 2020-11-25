package capstone.oras.model.oras_ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessJdRequest {
    private String jd;

    public ProcessJdRequest() {
    }

    public ProcessJdRequest(String jd) {
        this.jd = jd;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }
}
