package capstone.oras.model.custom;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationModel {
    private String type;
    private Integer numb;
    private String actor;
    private String jobTitle;
    private LocalDateTime lastModify;
    private List<Integer> ids;

    public NotificationModel() {
    }

    /**
     * Register
     *
     * @param type
     * @param numb
     * @param ids
     */
    public NotificationModel(String type, Integer numb, LocalDateTime lastModify, List<Integer> ids) {
        this.type = type;
        this.numb = numb;
        this.lastModify = lastModify;
        this.ids = ids;
    }

    /**
     * Modify
     *
     * @param type
     * @param actor
     * @param ids
     */
    public NotificationModel(String type, String actor, LocalDateTime lastModify, List<Integer> ids) {
        this.type = type;
        this.actor = actor;
        this.lastModify = lastModify;
        this.ids = ids;
    }

    /**
     * Apply
     *
     * @param type
     * @param numb
     * @param jobTitle
     * @param ids
     */
    public NotificationModel(String type, Integer numb, String jobTitle, LocalDateTime lastModify, List<Integer> ids) {
        this.type = type;
        this.numb = numb;
        this.jobTitle = jobTitle;
        this.lastModify = lastModify;
        this.ids = ids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumb() {
        return numb;
    }

    public void setNumb(Integer numb) {
        this.numb = numb;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public LocalDateTime getLastModify() {
        return lastModify;
    }

    public void setLastModify(LocalDateTime lastModify) {
        this.lastModify = lastModify;
    }
}
