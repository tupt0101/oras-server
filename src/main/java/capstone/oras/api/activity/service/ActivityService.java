package capstone.oras.api.activity.service;

import capstone.oras.dao.IActivityRepository;
import capstone.oras.entity.ActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements IActivityService {

    @Autowired
    private IActivityRepository IActivityRepository;


    @Override
    public ActivityEntity createActivity(ActivityEntity activityEntity) {
        return IActivityRepository.save(activityEntity);
    }

    @Override
    public ActivityEntity updateActivity(ActivityEntity activityEntity) {
        return IActivityRepository.save(activityEntity);
    }

    @Override
    public List<ActivityEntity> getAllActivities() {
        return IActivityRepository.findAll();
    }

    @Override
    public ActivityEntity findActivyById(int id) {
        if (IActivityRepository.findById(id).isPresent()) {
            return IActivityRepository.findById(id).get();
        } else return null;
    }

    @Override
    public List<ActivityEntity> findActivitiesByCreatorId(int creatorId) {
        if (IActivityRepository.findActivityEntitiesByCreatorIdEquals(creatorId).isPresent()) {
            return IActivityRepository.findActivityEntitiesByCreatorIdEquals(creatorId).get();
        } else return null;
    }
}
