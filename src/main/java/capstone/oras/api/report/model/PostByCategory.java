package capstone.oras.api.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostByCategory {
    private String category;
    private int numOfPost;

    public PostByCategory() {
    }

}
