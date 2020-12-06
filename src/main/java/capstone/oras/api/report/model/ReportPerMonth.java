package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPerMonth {
    private String month;
    private double amount;

    public ReportPerMonth(String month, double amount) {
        this.month = month;
        this.amount = amount;
    }

    public ReportPerMonth() {
    }
}
