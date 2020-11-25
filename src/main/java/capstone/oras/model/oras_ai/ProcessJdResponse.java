package capstone.oras.model.oras_ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessJdResponse {
    private String prc_jd;

    public ProcessJdResponse() {
    }

    public ProcessJdResponse(String prc_jd) {
        this.prc_jd = prc_jd;
    }

    public String getPrc_jd() {
        return prc_jd;
    }

    public void setPrc_jd(String prc_jd) {
        this.prc_jd = prc_jd;
    }
}
