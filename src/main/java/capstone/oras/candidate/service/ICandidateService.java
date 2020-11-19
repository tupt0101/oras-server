package capstone.oras.candidate.service;

import capstone.oras.entity.CandidateEntity;

import java.util.List;

public interface ICandidateService {
    CandidateEntity createCandidate (CandidateEntity candidateEntity);
    CandidateEntity updateCandidate (CandidateEntity candidateEntity);
    List<CandidateEntity> getAllCandidate();
    CandidateEntity findCandidateById(int id);
    List<CandidateEntity> findCandidatesByJobId(int jobID);
}
