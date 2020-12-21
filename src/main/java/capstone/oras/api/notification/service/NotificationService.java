package capstone.oras.api.notification.service;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.dao.INotificationRepository;
import capstone.oras.entity.NotificationEntity;
import capstone.oras.model.custom.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static capstone.oras.common.Constant.AccountRole.ADMIN;
import static capstone.oras.common.Constant.NotiType.*;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    INotificationRepository notificationRepository;
    @Autowired
    IAccountService accountService;
    @Autowired
    ICompanyService companyService;
    @Autowired
    IJobService jobService;


    @Override
    public NotificationEntity createNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public NotificationEntity updateNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationEntity> updateNotifications(List<NotificationEntity> notificationEntity) {
        return notificationRepository.saveAll(notificationEntity);
    }

    @Override
    public List<NotificationEntity> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public List<NotificationEntity> getAllAccountNotification(int accountId) {
        if (notificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).isPresent()) {
            return notificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public List<NotificationEntity> getAllNewAccountNotification(int accountId) {
        if (notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).isPresent()) {
            return notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public List<NotificationModel> getAllNewAccountNotification(int accountId, String role) {
        List<NotificationModel> res = new ArrayList<>();
        NotificationModel resNoti;
        if (ADMIN.equalsIgnoreCase(role)) {
            accountId = 0;
        }
        List<NotificationEntity> listNoti =
                notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).orElse(new ArrayList<>());
        if (listNoti.isEmpty()) {
            return res;
        }
        List<NotificationEntity> registerList =
                listNoti.stream().filter(o -> o.getType().equalsIgnoreCase(REGISTER)).collect(Collectors.toList());
        List<NotificationEntity> modifyList =
                listNoti.stream().filter(o -> o.getType().equalsIgnoreCase(MODIFY)).collect(Collectors.toList());
        List<NotificationEntity> applyList =
                listNoti.stream().filter(o -> o.getType().equalsIgnoreCase(APPLY)).collect(Collectors.toList());
        // handle REGISTER notification
        if (registerList.size() > 0) {
            registerList.sort(Comparator.comparing(NotificationEntity::getCreateDate));
            resNoti = new NotificationModel(REGISTER, registerList.size(), registerList.get(0).getCreateDate(),
                    registerList.stream().map(NotificationEntity::getId).collect(Collectors.toList()));
            res.add(resNoti);
        }
        // handle MODIFY notification
        for (NotificationEntity noti: modifyList) {
            String actor = companyService.getAccountCompany(noti.getTargetId()).getFullname();
            resNoti = new NotificationModel(MODIFY, actor, noti.getCreateDate(), Collections.singletonList(noti.getId()));
            res.add(resNoti);
        }
        // handle APPLY notification
        if (applyList.size() > 0) {
            Map<Integer, List<NotificationEntity>> jobIdToNoti = applyList.stream()
                    .collect(Collectors.groupingBy(NotificationEntity::getTargetId));
            for (Map.Entry<Integer, List<NotificationEntity>> noti: jobIdToNoti.entrySet()) {
                String title = jobService.getJobById(noti.getKey()).getTitle();
                noti.getValue().sort(Comparator.comparing(NotificationEntity::getCreateDate));
                resNoti = new NotificationModel(APPLY, noti.getValue().size(), title,
                        noti.getValue().get(0).getCreateDate(),
                        noti.getValue().stream().map(NotificationEntity::getId).collect(Collectors.toList()));
                res.add(resNoti);
            }
        }
        res.sort(Comparator.comparing(NotificationModel::getLastModify));
        return res;
    }

    @Override
    public NotificationEntity getNotificationById(int id) {
        if (notificationRepository.findById(id).isPresent()) {
            return notificationRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Integer readNotification(int id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification ID not found");
        }
        return notificationRepository.updateIsNew(id, false);
    }

    @Override
    public Integer readNotifications(List<Integer> ids) {
        return notificationRepository.updateIsNew(ids, false);
    }
}
