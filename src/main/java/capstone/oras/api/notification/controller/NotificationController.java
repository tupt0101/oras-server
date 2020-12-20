package capstone.oras.api.notification.controller;


import capstone.oras.api.notification.service.INotificationService;
import capstone.oras.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(path = "/v1/notification-management")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;


    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<NotificationEntity> createNotification(@RequestBody NotificationEntity notificationEntity) {
        return new ResponseEntity<>(notificationService.createNotification(notificationEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<NotificationEntity>> getAllNotification() {
        List<NotificationEntity> lst = notificationService.getAllNotification();

        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/notification/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<NotificationEntity> getNotificationById(@PathVariable("id") int id) {
        return new ResponseEntity<>(notificationService.getNotificationById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account-notifications/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<NotificationEntity>> getNotificationByAccountId(@PathVariable("id") int id) {
        return new ResponseEntity<>(notificationService.getAllAccountNotification(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/new-account-notifications/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<NotificationEntity>> getNewNotificationByAccountId(@PathVariable("id") int id) {
        return new ResponseEntity<>(notificationService.getAllNewAccountNotification(id), HttpStatus.OK);
    }
}
