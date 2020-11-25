package capstone.oras.model.oras_ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalcSimilarityResponse {
    String message;

    public CalcSimilarityResponse() {
    }

    public CalcSimilarityResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
