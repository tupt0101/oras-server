package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SystemAndUserSalaryReport {
    private List<SalaryByCategory> system;
    private List<SalaryByCategory> user;

    public List<SalaryByCategory> getSystem() {
        return system;
    }

    public void setSystem(List<SalaryByCategory> system) {
        this.system = system;
    }

    public List<SalaryByCategory> getUser() {
        return user;
    }

    public void setUser(List<SalaryByCategory> user) {
        this.user = user;
    }
}
