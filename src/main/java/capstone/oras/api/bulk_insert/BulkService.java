package capstone.oras.api.bulk_insert;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.email.service.EmailSenderService;
import capstone.oras.dao.IAccountRepository;
import capstone.oras.dao.IConfirmationTokenRepository;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.entity.openjob.OpenjobCompanyEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class BulkService implements IBulkService{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    IConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private ICompanyService companyService;
    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity entity;

    @Override
    public Integer signup(List<BulkController.Signup> signups) {
        int res = 0;
        //get openjob token
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
        String token = userDetailsService.getOpenJobToken();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        entity = new HttpEntity(headers);
        for (BulkController.Signup signup: signups) {
            try {
                this.validateSignUp(signup);
                signup = this.postToOpenJob(signup);
                saveAccountAndCompany(signup);
                res++;
            } catch (Exception e) {
                System.out.println(signup.accountEntity.getFullname() + ": creation error");
                System.out.println(e.getMessage());
            }
        }
        return res;
    }
    private void saveAccountAndCompany(BulkController.Signup signup) {
        CompanyEntity companyEntity = companyService.createCompany(signup.companyEntity);
        signup.accountEntity.setCompanyId(companyEntity.getId());
        signup.accountEntity.setPassword(passwordEncoder.encode(signup.accountEntity.getPassword()));
        signup.accountEntity.setCreateDate(LocalDateTime.now());
        accountRepository.save(signup.accountEntity);
    }

    private BulkController.Signup postToOpenJob(BulkController.Signup signup) {
        // post company to openjob
        String uri = "https://openjob-server.herokuapp.com/v1/company-management/company-by-name/" + signup.companyEntity.getName();
        // check company existence
        CompanyEntity openJobEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, CompanyEntity.class).getBody();
        if (openJobEntity == null) {
            OpenjobCompanyEntity openjobCompanyEntity = new OpenjobCompanyEntity();
            // ????? Why set 1
            openjobCompanyEntity.setAccountId(1);
            openjobCompanyEntity.setAvatar(signup.companyEntity.getAvatar());
            openjobCompanyEntity.setDescription(signup.companyEntity.getDescription());
            openjobCompanyEntity.setEmail(signup.companyEntity.getEmail());
            openjobCompanyEntity.setLocation(signup.companyEntity.getLocation());
            openjobCompanyEntity.setName(signup.companyEntity.getName());
            openjobCompanyEntity.setPhoneNo(signup.companyEntity.getPhoneNo());
            openjobCompanyEntity.setTaxCode(signup.companyEntity.getTaxCode());
            uri = "https://openjob-server.herokuapp.com/v1/company-management/company";
            HttpEntity<OpenjobCompanyEntity> httpCompanyEntity = new HttpEntity<>(openjobCompanyEntity, headers);
            // Register
            openjobCompanyEntity = restTemplate.postForObject(uri, httpCompanyEntity, OpenjobCompanyEntity.class);
            signup.companyEntity.setOpenjobCompanyId(openjobCompanyEntity != null ? openjobCompanyEntity.getId() : 0);
        } else {
            signup.companyEntity.setOpenjobCompanyId(openJobEntity.getId());
        }
        return signup;
    }

    private void validateSignUp(BulkController.Signup signup) {
        if (StringUtils.isEmpty(signup.accountEntity.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (StringUtils.isEmpty(signup.accountEntity.getFullname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        } else if (StringUtils.isEmpty(signup.accountEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        } else if (accountService.findAccountByEmail(signup.accountEntity.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountRepository.existsById(signup.accountEntity.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        }
    }
}
