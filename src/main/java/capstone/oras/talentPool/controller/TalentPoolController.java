package capstone.oras.talentPool.controller;

import capstone.oras.entity.TalentPoolEntity;
import capstone.oras.talentPool.service.ITalentPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/talentPool-management")
public class TalentPoolController {
    @Autowired
    private ITalentPoolService talentPoolService;


    @RequestMapping(value = "/talentPools", method = RequestMethod.GET)
    @ResponseBody
    List<TalentPoolEntity> getAllTalentPool() {
        return talentPoolService.getAllTalentPool();
    }

    @RequestMapping(value = "/createTalentPool", method = RequestMethod.POST)
    @ResponseBody
    TalentPoolEntity createTalentPool(@RequestBody TalentPoolEntity talentPoolEntity) {
        return talentPoolService.createTalentPool(talentPoolEntity);
    }

    @RequestMapping(value = "/updateTalentPool/{id}", method = RequestMethod.PUT)
    @ResponseBody
    TalentPoolEntity updateTalentPool(@RequestBody TalentPoolEntity talentPoolEntity) {
        return talentPoolService.updateTalentPool(talentPoolEntity);
    }
}
