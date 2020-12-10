package capstone.oras.api.bulk_insert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBulkService {
    Integer signup(List<BulkController.Signup> signups);
}
