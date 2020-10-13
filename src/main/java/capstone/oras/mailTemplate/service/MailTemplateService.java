package capstone.oras.mailTemplate.service;

import capstone.oras.dao.IMailTemplateRepository;
import capstone.oras.entity.MailTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailTemplateService implements IMailTemplateService {

    @Autowired
    private IMailTemplateRepository IMailTemplateRepository;


    @Override
    public MailTemplateEntity createMailTemplate(MailTemplateEntity mailTemplateEntity) {
        return IMailTemplateRepository.save(mailTemplateEntity);
    }

    @Override
    public MailTemplateEntity updateMailTemplate(MailTemplateEntity mailTemplateEntity) {
        return IMailTemplateRepository.save(mailTemplateEntity);
    }

    @Override
    public List<MailTemplateEntity> getAllMailTemplate() {
        return IMailTemplateRepository.findAll();
    }
}
