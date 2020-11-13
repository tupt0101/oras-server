package capstone.oras.job.constant;

public enum JobStatus {
    DRAFT("Draft"), OPEN("Published"), ONHOLD("On Hold"), CLOSED("Closed");

    private String value;

    JobStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
