package capstone.oras.talentPool.service;

import capstone.oras.dao.ITalentPoolRepository;
import capstone.oras.entity.TalentPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalentPoolService implements ITalentPoolService{
    @Autowired
    private ITalentPoolRepository ITalentPoolRepository;


    @Override
    public TalentPoolEntity createTalentPool(TalentPoolEntity talentPool) {
        return ITalentPoolRepository.save(talentPool);
    }

    @Override
    public TalentPoolEntity updateTalentPool(TalentPoolEntity talentPool) {
        return ITalentPoolRepository.save(talentPool);
    }

    @Override
    public List<TalentPoolEntity> getAllTalentPool() {
        return ITalentPoolRepository.findAll();
    }

    @Override
    public TalentPoolEntity findTalentPoolEntityById(int id) {
        if(ITalentPoolRepository.findById(id).isPresent()) {
            return ITalentPoolRepository.findById(id).get();
        } else return null;
    }
}
