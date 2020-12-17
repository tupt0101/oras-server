package capstone.oras.dao;

import capstone.oras.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Integer> {
    List<CandidateEntity> findCandidateEntitiesByEmailEquals(String email);
}
