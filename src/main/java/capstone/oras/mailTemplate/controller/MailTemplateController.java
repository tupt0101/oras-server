package capstone.oras.mailTemplate.controller;

import capstone.oras.entity.MailTemplateEntity;
import capstone.oras.mailTemplate.service.IMailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/mail-template-management")
public class MailTemplateController {

    @Autowired
    private IMailTemplateService mailTemplateService;

    @RequestMapping(value = "/mail-templates", method = RequestMethod.GET)
    @ResponseBody
    List<MailTemplateEntity> getAll() {
        return mailTemplateService.getAllMailTemplate();
    }

    @RequestMapping(value = "/mail-template", method = RequestMethod.POST)
    @ResponseBody
    MailTemplateEntity createMailTemplate(@RequestBody MailTemplateEntity mailTemplateEntity){
        return mailTemplateService.createMailTemplate(mailTemplateEntity);
    }

    @RequestMapping(value = "/mail-template", method = RequestMethod.PUT)
    @ResponseBody
    MailTemplateEntity updateMailTemplate(@RequestBody MailTemplateEntity mailTemplateEntity){
        return mailTemplateService.createMailTemplate(mailTemplateEntity);
    }


}
