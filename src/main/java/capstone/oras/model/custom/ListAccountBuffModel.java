package capstone.oras.model.custom;

import java.util.List;

public class ListAccountBuffModel {
    Integer total;
    List<AccountBuffModel> data;

    public ListAccountBuffModel(Integer total, List<AccountBuffModel> data) {
        this.total = total;
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<AccountBuffModel> getData() {
        return data;
    }

    public void setData(List<AccountBuffModel> data) {
        this.data = data;
    }
}
