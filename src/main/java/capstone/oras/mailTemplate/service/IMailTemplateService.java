package capstone.oras.mailTemplate.service;

import capstone.oras.entity.MailTemplateEntity;

import java.util.List;

public interface IMailTemplateService {
    MailTemplateEntity createMailTemplate(MailTemplateEntity mailTemplateEntity);
    MailTemplateEntity updateMailTemplate(MailTemplateEntity mailTemplateEntity);
    List<MailTemplateEntity> getAllMailTemplate();
}
