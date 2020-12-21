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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumOfApplication() {
        return numOfApplication;
    }

    public void setNumOfApplication(int numOfApplication) {
        this.numOfApplication = numOfApplication;
    }
}
