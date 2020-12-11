package capstone.oras.model.custom;

import capstone.oras.entity.CompanyEntity;

import java.util.List;

public class ListCompanyModel {
    Integer total;
    List<CompanyEntity> data;

    public ListCompanyModel(Integer total, List<CompanyEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<CompanyEntity> getData() {
        return data;
    }

    public void setData(List<CompanyEntity> data) {
        this.data = data;
    }
}
