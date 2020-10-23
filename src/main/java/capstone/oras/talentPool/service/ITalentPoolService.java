package capstone.oras.talentPool.service;

import capstone.oras.entity.TalentPoolEntity;

import java.util.List;

public interface ITalentPoolService {
    TalentPoolEntity createTalentPool(TalentPoolEntity talentPool);
    TalentPoolEntity updateTalentPool(TalentPoolEntity talentPool);
    List<TalentPoolEntity> getAllTalentPool();
    TalentPoolEntity findTalentPoolEntityById(int id);
}
