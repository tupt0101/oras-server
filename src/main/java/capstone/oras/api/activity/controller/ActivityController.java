package capstone.oras.api.activity.controller;

import capstone.oras.api.activity.service.IActivityService;
import capstone.oras.entity.ActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(path = "/v1/activity-management")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    HttpHeaders httpHeaders = new HttpHeaders();

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ActivityEntity>> getAllActivity() {
        return new ResponseEntity<List<ActivityEntity>>(activityService.getAllActivities(), HttpStatus.OK);

    }

    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<ActivityEntity> createActivity(@RequestBody ActivityEntity activityEntity) {
        return new ResponseEntity<ActivityEntity>(activityService.createActivity(activityEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/activity", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<ActivityEntity> updateActivity(@RequestBody ActivityEntity activityEntity) {
        return new ResponseEntity<ActivityEntity>(activityService.createActivity(activityEntity), HttpStatus.OK);

    }

    @RequestMapping(value = "/activity-by-creator-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<ActivityEntity>> getActivitiesByCreatorId(@PathVariable("id") int creatorId) {
        List<ActivityEntity> activityEntityList = activityService.findActivitiesByCreatorId(creatorId);
        activityEntityList.sort(Comparator.comparing(ActivityEntity::getTime).reversed());
        return new ResponseEntity<List<ActivityEntity>>(activityEntityList, HttpStatus.OK);

    }
}
