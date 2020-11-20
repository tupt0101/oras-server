package capstone.oras.dao;

import capstone.oras.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Integer> {
    Optional<List<CandidateEntity>> findCandidateEntitiesByEmailEquals(String email);
}
