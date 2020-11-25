package capstone.oras.api.job;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;

public class JobValidation {
    public void validCreatorId(Integer val) {
        if (val == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreatorId is null");
        }
    }
    public void validTitle(String val) {
        if (val == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is null");
        }
    }
    public void validApplyFrom(LocalDateTime val) {
        if (val == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply From is null");
        }
    }
    public void validApplyTo(LocalDateTime val) {
        if (val == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apply To is null");
        }
    }
}
