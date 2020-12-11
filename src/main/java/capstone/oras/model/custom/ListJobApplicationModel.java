package capstone.oras.model.custom;

import capstone.oras.entity.JobApplicationEntity;

import java.util.List;

public class ListJobApplicationModel {
    Integer total;
    List<JobApplicationEntity> data;

    public ListJobApplicationModel(Integer total, List<JobApplicationEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<JobApplicationEntity> getData() {
        return data;
    }

    public void setData(List<JobApplicationEntity> data) {
        this.data = data;
    }
}
