package capstone.oras.model.custom;

import capstone.oras.entity.JobEntity;

import java.util.List;

public class ListJobModel {
    Integer total;
    List<JobEntity> data;

    public ListJobModel(Integer total, List<JobEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<JobEntity> getData() {
        return data;
    }

    public void setData(List<JobEntity> data) {
        this.data = data;
    }
}
