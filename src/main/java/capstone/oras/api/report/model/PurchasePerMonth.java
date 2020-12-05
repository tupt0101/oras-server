package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasePerMonth {
    private String month;
    private double amount;

    public PurchasePerMonth(String month, double amount) {
        this.month = month;
        this.amount = amount;
    }

    public PurchasePerMonth() {
    }
}
