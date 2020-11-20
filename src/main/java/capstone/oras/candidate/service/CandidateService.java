package capstone.oras.candidate.service;

import capstone.oras.dao.ICandidateRepository;
import capstone.oras.entity.CandidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService implements ICandidateService{

    @Autowired
    private ICandidateRepository ICandidateRepository;

    @Override
    public CandidateEntity createCandidate(CandidateEntity candidateEntity) {
        return ICandidateRepository.save(candidateEntity);
    }

    @Override
    public CandidateEntity updateCandidate(CandidateEntity candidateEntity) {
        return ICandidateRepository.save(candidateEntity);
    }

    @Override
    public List<CandidateEntity> getAllCandidate() {
        return ICandidateRepository.findAll();
    }

    @Override
    public CandidateEntity findCandidateById(int id) {
        if (ICandidateRepository.findById(id).isPresent()) {
            return ICandidateRepository.findById(id).get();
        } else return null;
    }

    @Override
    public List<CandidateEntity> findCandidatesByJobId(int jobID) {
        return null;
    }

    @Override
    public List<CandidateEntity> findCandidatesByEmail(String email) {
        if (ICandidateRepository.findCandidateEntitiesByEmailEquals(email).isPresent()) {
            return ICandidateRepository.findCandidateEntitiesByEmailEquals(email).get();
        } else return null;
    }
}
