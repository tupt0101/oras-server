package capstone.oras.api.talentPool.controller;

import capstone.oras.api.talentPool.service.ITalentPoolService;
import capstone.oras.entity.TalentPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/talent-pool-management")
public class TalentPoolController {
    @Autowired
    private ITalentPoolService talentPoolService;

    HttpHeaders httpHeaders = new HttpHeaders();


    @RequestMapping(value = "/talentPools", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<TalentPoolEntity>> getAllTalentPool() {
        List<TalentPoolEntity> lst = talentPoolService.getAllTalentPool();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(TalentPoolEntity::getId));
        }
        return new ResponseEntity<List<TalentPoolEntity>>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/talentPool/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<TalentPoolEntity> getTalentPoolById(@PathVariable("id")int id) {
        return new ResponseEntity<TalentPoolEntity>(talentPoolService.findTalentPoolEntityById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/talentPool", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<TalentPoolEntity> createTalentPool(@RequestBody TalentPoolEntity talentPoolEntity) {
        if (talentPoolEntity.getName() == null || talentPoolEntity.getName().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is empty");
        } else if (talentPoolService.findTalentPoolEntityById(talentPoolEntity.getId()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID already exist");
        }
        return new ResponseEntity<>(talentPoolService.createTalentPool(talentPoolEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/talentPool", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<TalentPoolEntity> updateTalentPool(@RequestBody TalentPoolEntity talentPoolEntity) {
        if (talentPoolEntity.getName() == null || talentPoolEntity.getName().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is empty");
        } else if (talentPoolService.findTalentPoolEntityById(talentPoolEntity.getId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Talent Pool ID is not exist");
        }
        return new ResponseEntity<>(talentPoolService.updateTalentPool(talentPoolEntity), HttpStatus.OK);
    }

}
