package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryByCategory {
    private String category;
    private int averageSalary;

    public SalaryByCategory() {
    }
}
