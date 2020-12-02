package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationByCategory {
    private String category;
    private int numOfApplication;

    public ApplicationByCategory() {
    }
}
