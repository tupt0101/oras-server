package capstone.oras.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic {
    private int totalJob;
    private int totalPublishJob;
    private int totalCandidate;
    private int totalHiredCandidate;

    public Statistic() {
    }
}
