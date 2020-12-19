package capstone.oras.api.company.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.ICompanyRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.ListAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static capstone.oras.common.Constant.EmailForm.VERIFY_COMPANY_NOTI;

@Service
public class CompanyService implements ICompanyService{

    @Autowired
    private ICompanyRepository ICompanyRepository;
    @Autowired
    private IAccountRepository iAccountRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        return ICompanyRepository.save(companyEntity);
    }

    @Override
    public CompanyEntity updateCompany(CompanyEntity companyEntity) {
        return ICompanyRepository.save(companyEntity);
    }

    @Override
    public List<CompanyEntity> getAllCompany() {
        return ICompanyRepository.findAll();
    }

    @Override
    public ListAccountModel getAllCompanyWithPaging(Pageable pageable, String status, String name) {
        name = "%" + name + "%";
        int count;
        List<AccountEntity> data;
        if (StringUtils.isEmpty(status)) {
            data =  ICompanyRepository.accountCompanyPagingFilterName(pageable, name);
            count =  ICompanyRepository.countByNameIgnoreCaseLike(name);
        } else {
            data = ICompanyRepository.accountCompanyPagingFilter(pageable, "Verified".equalsIgnoreCase(status), name);
            count = ICompanyRepository.countByVerifiedAndNameIgnoreCaseLike("Verified".equalsIgnoreCase(status), name);
        }
        return new ListAccountModel(count, data);
    }

    @Override
    public CompanyEntity findCompanyById(int id) {
        if (ICompanyRepository.findById(id).isPresent()) {
            return ICompanyRepository.findById(id).get();
        } else return null;
    }

    @Override
    public List<CompanyEntity> getAllCompanyWithNameAndIsVerified(String name) {
        if(ICompanyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name, true).isPresent()) {
            return ICompanyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name,true).get();
        } else return null;
    }

    @Override
    public Boolean checkCompanyName(Integer id, String name) {
        if (id == null) {
            id = 0;
        }
        return ICompanyRepository.findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(id, name, true).isPresent();
    }

    @Override
    public Integer verifyCompany(int id, String email) throws MessagingException {
        if (!ICompanyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist.");
        }
        iAccountRepository.updateActiveByVerifyingCompany(id);
        this.sendMail(email, "Complete Registration!",VERIFY_COMPANY_NOTI);
        return ICompanyRepository.verifyCompanyPass(id);
    }

    @Override
    public AccountEntity getAccountCompany(int id) {
        return ICompanyRepository.accountCompanyById(id);
    }

    public void sendMail(String email, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(subject);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        // use the true flag to indicate the text included is HTML
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}
