package capstone.oras.candidate.controller;


import capstone.oras.candidate.service.ICandidateService;
import capstone.oras.entity.CandidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/candidate-management")
public class CandidateController {

    @Autowired
    private ICandidateService candidateService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/candidate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<CandidateEntity> createCandidate(@RequestBody CandidateEntity candidateEntity) {

        return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidate", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<CandidateEntity> updateCandidate(@RequestBody CandidateEntity candidateEntity) {

        return new ResponseEntity<>(candidateService.createCandidate(candidateEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidates", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<CandidateEntity>> getAllCandidate() {

        return new ResponseEntity<List<CandidateEntity>>(candidateService.getAllCandidate(), HttpStatus.OK);

    }

    @RequestMapping(value = "/candidate/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<CandidateEntity> getCandidateById(@PathVariable("id") int id) {
        return new ResponseEntity<CandidateEntity>(candidateService.findCandidateById(id), HttpStatus.OK);
    }
}
