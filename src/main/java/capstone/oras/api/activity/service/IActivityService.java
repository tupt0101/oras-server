package capstone.oras.api.activity.service;

import capstone.oras.entity.ActivityEntity;

import java.util.List;

public interface IActivityService {
    ActivityEntity createActivity (ActivityEntity activityEntity);
    ActivityEntity updateActivity (ActivityEntity activityEntity);
    List<ActivityEntity> getAllActivities();
    ActivityEntity findActivyById(int id);
    List<ActivityEntity> findActivitiesByCreatorId(int creatorId);

}
