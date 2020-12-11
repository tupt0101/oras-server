package capstone.oras.model.custom;

import capstone.oras.entity.AccountEntity;

import java.util.List;

public class ListAccountModel {
    Integer total;
    List<AccountEntity> data;

    public ListAccountModel(Integer total, List<AccountEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AccountEntity> getData() {
        return data;
    }

    public void setData(List<AccountEntity> data) {
        this.data = data;
    }
}
