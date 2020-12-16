package capstone.oras.model.custom;

import java.util.List;

public class ListCompanyModel {
    Integer total;
    List<CustomCompanyEntity> data;

    public ListCompanyModel(Integer total, List<CustomCompanyEntity> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<CustomCompanyEntity> getData() {
        return data;
    }

    public void setData(List<CustomCompanyEntity> data) {
        this.data = data;
    }
}
