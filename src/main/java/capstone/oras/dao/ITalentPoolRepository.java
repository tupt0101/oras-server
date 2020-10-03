package capstone.oras.dao;

import capstone.oras.entity.TalentPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITalentPoolRepository extends JpaRepository<TalentPoolEntity, Integer> {
}
