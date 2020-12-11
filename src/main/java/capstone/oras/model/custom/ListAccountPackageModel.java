package capstone.oras.model.custom;

import capstone.oras.entity.AccountPackageEntity;

import java.util.List;

public class ListAccountPackageModel {
    Integer total;
    List<AccountPackageEntity> data;

    public ListAccountPackageModel(Integer total, List<AccountPackageEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AccountPackageEntity> getData() {
        return data;
    }

    public void setData(List<AccountPackageEntity> data) {
        this.data = data;
    }
}
