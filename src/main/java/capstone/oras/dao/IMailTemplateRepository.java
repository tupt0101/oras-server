package capstone.oras.dao;

import capstone.oras.entity.MailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMailTemplateRepository extends JpaRepository<MailTemplateEntity, Integer> {
}
