package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SystemAndUserSalaryReport {
    private List<SalaryByCategory> system;
    private List<SalaryByCategory> user;
}
