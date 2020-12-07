package capstone.oras.common;

public class Constant {
    public static final String AI_PROCESS_HOST = "http://127.0.0.1:5000";
    public static final String OPEN_JOB_HOST = "https://openjob-server.herokuapp.com";
    public static class JobStatus {
        public static final String DRAFT = "Draft";
        public static final String PUBLISHED = "Published";
        public static final String CLOSED = "Closed";
    }
    public static class AccountRole{
        public static final String ADMIN = "admin";
        public static final String USER = "user";
    }
    public static class ApplicantStatus {
        public static final String HIRED = "Hired";
    }
}

