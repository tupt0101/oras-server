package capstone.oras.api.company.service;

import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.IBuffCompanyRepository;
import capstone.oras.dao.ICompanyRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.BuffCompanyEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.model.custom.AccountBuffModel;
import capstone.oras.model.custom.ListAccountBuffModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static capstone.oras.common.Constant.EmailForm.*;
import static capstone.oras.common.Constant.TIME_ZONE;

@Service
public class CompanyService implements ICompanyService{

    @Autowired
    private ICompanyRepository companyRepository;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IBuffCompanyRepository bufferCompanyRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    private RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Override
    public CompanyEntity createCompany(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    @Override
    public CompanyEntity updateCompany(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    @Override
    public CompanyEntity updateCompanyByAdmin(CompanyEntity companyEntity) throws MessagingException {
        String toEmail = getAccountCompany(companyEntity.getId()).getEmail();
        CompanyEntity current = findCompanyById(companyEntity.getId());
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company doesn't exist");
        }
        CompanyEntity raw = new CompanyEntity();
        raw.setName(current.getName());
        raw.setEmail(current.getEmail());
        raw.setPhoneNo(current.getPhoneNo());
        raw.setLocation(current.getLocation());
        raw.setDescription(current.getDescription());
        if (StringUtils.isEmpty(companyEntity.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        }
        if (StringUtils.isEmpty(companyEntity.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        }
        if (StringUtils.isEmpty(companyEntity.getPhoneNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is a required field");
        }
        if (StringUtils.isEmpty(companyEntity.getLocation())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is a required field");
        }
        companyEntity.setModifyDate(LocalDateTime.now(TIME_ZONE));
        CompanyEntity ret;
        ret = companyRepository.save(companyEntity);
        this.sendMail(toEmail, "Your company information has been changed", updateCompanyNoti(companyEntity, raw));
        return ret;
    }

    @Override
    public List<CompanyEntity> getAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    public ListAccountBuffModel getAllCompanyWithPaging(Pageable pageable, String status, String name) {
        name = "%" + name + "%";
        int count;
        List<AccountEntity> data;
        if (StringUtils.isEmpty(status)) {
            data =  companyRepository.accountCompanyPagingFilterName(pageable, name);
            count =  companyRepository.countByNameIgnoreCaseLike(name);
        } else {
            data = companyRepository.accountCompanyPagingFilter(pageable, "Verified".equalsIgnoreCase(status), name);
            count = companyRepository.countByVerifiedAndNameIgnoreCaseLike("Verified".equalsIgnoreCase(status), name);
        }
        // replace with company in buffer
        List<AccountBuffModel> result = new ArrayList<>();
        AccountBuffModel buff;
        for (AccountEntity comp: data) {
            buff = new AccountBuffModel();
            BeanUtils.copyProperties(comp, buff);
            if (!buff.getCompanyById().getVerified()) {
                Optional<BuffCompanyEntity> temp = bufferCompanyRepository.findById(comp.getCompanyId());
                buff.setBuffCompany(temp.orElse(null));
            }
            result.add(buff);
        }
        return new ListAccountBuffModel(count, result);
    }

    @Override
    public CompanyEntity findCompanyById(int id) {
        if (companyRepository.existsById(id)) {
            return companyRepository.findById(id).get();
        } else return null;
    }

    @Override
    public List<CompanyEntity> getAllCompanyWithNameAndIsVerified(String name) {
        if(companyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name, true).isPresent()) {
            return companyRepository.findCompanyEntitiesByNameEqualsAndVerifiedEquals(name,true).get();
        } else return null;
    }

    @Override
    public Boolean checkCompanyName(Integer id, String name) {
        if (id == null) {
            id = 0;
        }
        return companyRepository.findCompanyEntitiesByIdIsNotAndNameEqualsAndVerifiedEquals(id, name, true).isPresent();
    }

    @Override
    public void verifyCompany(int id, String email) throws MessagingException {
        CompanyEntity companyEntity = new CompanyEntity();
        String mailSubject;
        String mailText;
        if (!companyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company does not exist.");
        }
        if (bufferCompanyRepository.existsById(id)) {
            BuffCompanyEntity buffer = bufferCompanyRepository.findById(id).get();
            BeanUtils.copyProperties(buffer, companyEntity);
            companyEntity = companyRepository.save(companyEntity);
            bufferCompanyRepository.deleteById(id);
            // update company at OJ
            String uri = "https://openjob-server.herokuapp.com/v1/company-management/company";
            headers.setBearerAuth(CommonUtils.getOjToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<CompanyEntity> entity = new HttpEntity<>(companyEntity, headers);
            try {
                restTemplate.postForObject(uri, entity, CompanyEntity.class);
            } catch (HttpClientErrorException.Unauthorized e) {
                CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
                entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
                restTemplate.postForObject(uri, entity, CompanyEntity.class);
            }
            mailSubject = "Your company's new information has been accepted!";
            mailText = VERIFY_COMPANY_UPDATE_NOTI;
        } else {
            accountRepository.updateActiveByVerifyingCompany(id);
            mailSubject = "Complete Registration!";
            mailText = VERIFY_COMPANY_NOTI;
        }
        this.updateCompanyStatus(id, true);
        this.sendMail(email, mailSubject, mailText);
    }

    @Override
    public AccountEntity getAccountCompany(int id) {
        return companyRepository.accountCompanyById(id);
    }

    @Override
    public int updateCompanyStatus(int id, boolean status) {
        return companyRepository.updateCompanyStatus(id, status);
    }

    @Override
    public BuffCompanyEntity saveBufferCompany(CompanyEntity companyEntity) {
        BuffCompanyEntity BuffCompanyEntity = new BuffCompanyEntity();
        BeanUtils.copyProperties(companyEntity, BuffCompanyEntity);
        return bufferCompanyRepository.save(BuffCompanyEntity);
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
