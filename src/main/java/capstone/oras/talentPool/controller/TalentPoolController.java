package capstone.oras.talentPool.controller;

import capstone.oras.talentPool.service.ITalentPoolService;
import capstone.oras.entity.TalentPoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/talent-pool-management")
public class TalentPoolController {
    @Autowired
    private ITalentPoolService talentPoolService;

    HttpHeaders httpHeaders = new HttpHeaders();


    @RequestMapping(value = "/talentPools", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<TalentPoolEntity>> getAllTalentPool() {
        List<TalentPoolEntity> lst = talentPoolService.getAllTalentPool();
        lst.sort(Comparator.comparingInt(TalentPoolEntity::getId));
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
            httpHeaders.set("error", "Name is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (talentPoolService.findTalentPoolEntityById(talentPoolEntity.getId()) != null) {
            httpHeaders.set("error", "Talent Pool ID already exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(talentPoolService.createTalentPool(talentPoolEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/talentPool", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<TalentPoolEntity> updateTalentPool(@RequestBody TalentPoolEntity talentPoolEntity) {
        if (talentPoolEntity.getName() == null || talentPoolEntity.getName().isEmpty()) {
            httpHeaders.set("error", "Name is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (talentPoolService.findTalentPoolEntityById(talentPoolEntity.getId()) == null) {
            httpHeaders.set("error", "Talent Pool ID is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(talentPoolService.updateTalentPool(talentPoolEntity), HttpStatus.OK);
    }

}
