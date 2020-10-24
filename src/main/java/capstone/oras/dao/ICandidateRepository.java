package capstone.oras.dao;

import capstone.oras.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Integer> {
}
