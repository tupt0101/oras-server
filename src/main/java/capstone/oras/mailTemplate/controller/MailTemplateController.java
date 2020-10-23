package capstone.oras.mailTemplate.controller;

import capstone.oras.account.service.IAccountService;
import capstone.oras.entity.MailTemplateEntity;
import capstone.oras.mailTemplate.service.IMailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/mail-template-management")
public class MailTemplateController {

    @Autowired
    private IMailTemplateService mailTemplateService;

    @Autowired
    private IAccountService accountService;

    HttpHeaders httpHeaders = new HttpHeaders();


    @RequestMapping(value = "/mail-templates", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<MailTemplateEntity>> getAll() {

        return new ResponseEntity<List<MailTemplateEntity>>(mailTemplateService.getAllMailTemplate(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mail-template", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<MailTemplateEntity> createMailTemplate(@RequestBody MailTemplateEntity mailTemplateEntity) {
        if (mailTemplateEntity.getSubject() == null || mailTemplateEntity.getSubject().isEmpty()) {
            httpHeaders.set("error", "Subject is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (mailTemplateEntity.getType() == null || mailTemplateEntity.getType().isEmpty()) {
            httpHeaders.set("error", "Type is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (mailTemplateService.findMailEntityById(mailTemplateEntity.getId()) != null) {
            httpHeaders.set("error", "Mail ID is already exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountEntityById(mailTemplateEntity.getCreatorId()) == null) {
            httpHeaders.set("error", "Account doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<MailTemplateEntity>(mailTemplateService.createMailTemplate(mailTemplateEntity), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/mail-template", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<MailTemplateEntity> updateMailTemplate(@RequestBody MailTemplateEntity mailTemplateEntity) {
        if (mailTemplateEntity.getSubject() == null || mailTemplateEntity.getSubject().isEmpty()) {
            httpHeaders.set("error", "Subject is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (mailTemplateEntity.getType() == null || mailTemplateEntity.getType().isEmpty()) {
            httpHeaders.set("error", "Type is empty");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (mailTemplateService.findMailEntityById(mailTemplateEntity.getId()) == null) {
            httpHeaders.set("error", "Mail Id is not exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else if (accountService.findAccountEntityById(mailTemplateEntity.getCreatorId()) == null) {
            httpHeaders.set("error", "Account doesn't exist");
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<MailTemplateEntity>(mailTemplateService.updateMailTemplate(mailTemplateEntity), HttpStatus.OK);
        }
    }


}
