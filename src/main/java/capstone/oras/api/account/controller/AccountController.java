package capstone.oras.api.account.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.job.service.IJobService;
import capstone.oras.common.CommonUtils;
import capstone.oras.dao.IConfirmationTokenRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.entity.ConfirmationToken;
import capstone.oras.entity.JobEntity;
import capstone.oras.entity.openjob.OpenjobCompanyEntity;
import capstone.oras.entity.openjob.OpenjobJobEntity;
import capstone.oras.model.custom.ListAccountModel;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static capstone.oras.common.Constant.EmailForm.confirmMail;
import static capstone.oras.common.Constant.EmailForm.resetPasswordMail;
import static capstone.oras.common.Constant.TIME_ZONE;


@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/account-management")
public class AccountController {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    IConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IJobService jobService;


    public AccountController(IAccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    static class Signup {
        public AccountEntity accountEntity;
        public CompanyEntity companyEntity;
    }

    static class PasswordChanges {
        public Integer accountId;
        public String currentPassword;
        public String newPassword;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity accountEntity) {
        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        } else if (accountEntity.getPassword() == null || accountEntity.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        } else if (accountService.findAccountByEmail(accountEntity.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountService.findAccountEntityById(accountEntity.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        } else {
            accountEntity.setCreateDate(LocalDateTime.now(TIME_ZONE));
            accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AccountEntity> signup(@RequestBody Signup signup) throws MessagingException {
        if (signup.accountEntity.getEmail() == null || signup.accountEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (signup.accountEntity.getFullname() == null || signup.accountEntity.getFullname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        } else if (signup.accountEntity.getPassword() == null || signup.accountEntity.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is a required field");
        } else if (accountService.findAccountByEmail(signup.accountEntity.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountService.findAccountEntityById(signup.accountEntity.getId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        } else if (signup.accountEntity.getPhoneNo() == null || signup.accountEntity.getPhoneNo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is a required field");
        }

        else if (signup.companyEntity.getEmail() == null || signup.companyEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (signup.companyEntity.getName() == null || signup.companyEntity.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is a required field");
        } else if (signup.companyEntity.getTaxCode() == null || signup.companyEntity.getTaxCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tax Code is a required field");
        } else if (signup.companyEntity.getPhoneNo() == null || signup.companyEntity.getPhoneNo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number is a required field");
        } else if (signup.companyEntity.getLocation() == null || signup.companyEntity.getLocation().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is a required field");
        } else if (companyService.checkCompanyName(signup.companyEntity.getId(), signup.companyEntity.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already exist");
        }

        else {
            signup.accountEntity.setCreateDate(LocalDateTime.now(TIME_ZONE));
            signup.companyEntity.setModifyDate(LocalDateTime.now(TIME_ZONE));
            //get openjob token
            CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
            String token = CommonUtils.getOjToken();
            // post company to openjob
            String uri = "https://openjob-server.herokuapp.com/v1/company-management/company-by-name/" + signup.companyEntity.getName();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
//        headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity(headers);
            CompanyEntity openJobEntity;
            try {
                openJobEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, CompanyEntity.class).getBody();
            } catch (HttpClientErrorException.Unauthorized e) {
                CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
                entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
                openJobEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, CompanyEntity.class).getBody();
            }
            if (openJobEntity == null) {
                OpenjobCompanyEntity openjobCompanyEntity = new OpenjobCompanyEntity();
                openjobCompanyEntity.setAccountId(1);
                if (!signup.companyEntity.getAvatar().isEmpty() || signup.companyEntity.getAvatar() != null) {
                    openjobCompanyEntity.setAvatar(signup.companyEntity.getAvatar());
                }
                if (!signup.companyEntity.getDescription().isEmpty() || signup.companyEntity.getDescription() != null) {
                    openjobCompanyEntity.setDescription(signup.companyEntity.getDescription());
                }
                if (!signup.companyEntity.getEmail().isEmpty() || signup.companyEntity.getEmail() != null) {
                    openjobCompanyEntity.setEmail(signup.companyEntity.getEmail());
                }
                if (!signup.companyEntity.getLocation().isEmpty() || signup.companyEntity.getLocation() != null) {
                    openjobCompanyEntity.setLocation(signup.companyEntity.getLocation());
                }
                if (!signup.companyEntity.getName().isEmpty() || signup.companyEntity.getName() != null) {
                    openjobCompanyEntity.setName(signup.companyEntity.getName());
                }
                if (!signup.companyEntity.getPhoneNo().isEmpty() || signup.companyEntity.getPhoneNo() != null) {
                    openjobCompanyEntity.setPhoneNo(signup.companyEntity.getPhoneNo());
                }
                if (!signup.companyEntity.getTaxCode().isEmpty() || signup.companyEntity.getTaxCode() != null) {
                    openjobCompanyEntity.setTaxCode(signup.companyEntity.getTaxCode());
                }
                uri = "https://openjob-server.herokuapp.com/v1/company-management/company";
                HttpEntity<OpenjobCompanyEntity> httpCompanyEntity = new HttpEntity<>(openjobCompanyEntity, headers);
                try {
                    openjobCompanyEntity = restTemplate.postForObject(uri, httpCompanyEntity, OpenjobCompanyEntity.class);
                } catch (HttpClientErrorException.Unauthorized e) {
                    CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
                    entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
                    openjobCompanyEntity = restTemplate.postForObject(uri, httpCompanyEntity, OpenjobCompanyEntity.class);
                }
                signup.companyEntity.setOpenjobCompanyId(openjobCompanyEntity.getId());
            } else {
                signup.companyEntity.setOpenjobCompanyId(openJobEntity.getId());
            }
//            signup.companyEntity.setVerified(false);
            CompanyEntity companyEntity = companyService.createCompany(signup.companyEntity);
            signup.accountEntity.setCompanyId(companyEntity.getId());
//            signup.accountEntity.setActive(false);
            signup.accountEntity.setConfirmMail(false);
            signup.accountEntity.setPassword(passwordEncoder.encode(signup.accountEntity.getPassword()));
            AccountEntity accountEntity = accountService.createAccount(signup.accountEntity);
            ConfirmationToken confirmationToken = new ConfirmationToken(accountEntity);
            confirmationTokenRepository.save(confirmationToken);
        this.sendMail(signup.accountEntity.getEmail(), "Complete Registration!",
                    confirmMail(confirmationToken.getConfirmationToken()));
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/resend-email", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Integer> resendEmail(@Param("email") String email){
        try {
            AccountEntity accountEntity = accountService.findAccountByEmail(email);
            ConfirmationToken confirmationToken = new ConfirmationToken(accountEntity);
            confirmationTokenRepository.save(confirmationToken);
        this.sendMail(email, "Complete Registration!",
                    confirmMail(confirmationToken.getConfirmationToken()));
            return new ResponseEntity<>(0, HttpStatus.OK);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Cannot send email.");
        }
    }



//    @RequestMapping(value = "/activate-account/{id}", method = RequestMethod.PUT)
//    @ResponseBody
//    ResponseEntity<AccountEntity> activeAccountViaCompany(@PathVariable("id") int companyId) throws MessagingException {
//        if (companyId == 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id is a required field");
//        } else if (companyService.findCompanyById(companyId) == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company doesn't exist");
//        }
//        // refactor code de update 1 field thoi dung native query nang cao hieu suat
//        CompanyEntity companyEntity = companyService.findCompanyById(companyId);
//        companyEntity.setVerified(true);
//        companyService.updateCompany(companyEntity);
//        AccountEntity accountEntity = accountService.findAccountByCompanyId(companyId);
//        accountEntity.setActive(true);
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        message.setSubject("Complete Registration!");
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(accountEntity.getEmail());
//
//        // use the true flag to indicate the text included is HTML
//        helper.setText(VERIFY_COMPANY_NOTI, true);
//        javaMailSender.send(message);
//        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
//    }

    @RequestMapping(value = "/deactivate-account/{accountId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AccountEntity> deactiveAccount(@PathVariable("accountId") int accountId) {

        // refactor code de update 1 field thoi dung native query nang cao hieu suat
        AccountEntity accountEntity = accountService.findAccountEntityById(accountId);
        accountEntity.setActive(false);
        List<JobEntity> jobEntities = jobService.getAllJobByCreatorId(accountId);
        for (JobEntity jobEntity : jobEntities) {
            int openjobJobId = jobEntity.getOpenjobJobId();
            //get openjob token
            String token = CommonUtils.getOjToken();
            // close job to openjob
            String uri = "https://openjob-server.herokuapp.com/v1/job-management/job/" + openjobJobId + "/close";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity(headers);
            // close job on openjob
            try {
                restTemplate.exchange(uri, HttpMethod.PUT, entity, OpenjobJobEntity.class);
            } catch (HttpClientErrorException.Unauthorized e) {
                CommonUtils.setOjToken(CommonUtils.getOpenJobToken());
                entity.getHeaders().setBearerAuth(CommonUtils.getOjToken());
                restTemplate.exchange(uri, HttpMethod.PUT, entity, OpenjobJobEntity.class);
            }
            jobService.closeJob(jobEntity.getId());
        }
        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate-by-account-id/{accountId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AccountEntity> activeAccount(@PathVariable("accountId") int accountId) {
        // refactor code de update 1 field thoi dung native query nang cao hieu suat
        AccountEntity accountEntity = accountService.findAccountEntityById(accountId);
        accountEntity.setActive(true);
        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AccountEntity> updateAccount(@RequestBody AccountEntity accountEntity) {

        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is a required field");
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        } else if (accountService.findAccountEntityById(accountEntity.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        } else {
            return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/change-password-account", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AccountEntity> changePassword(@RequestBody PasswordChanges passwordChanges) {
        AccountEntity accountEntity = accountService.findAccountEntityById(passwordChanges.accountId);
        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        } else if (!passwordEncoder.matches(passwordChanges.currentPassword, accountEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password doesn't match current password");
        }
        accountEntity.setPassword(passwordEncoder.encode(passwordChanges.newPassword));


        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/update-account", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Integer> customUpdateAccount(@RequestBody AccountEntity accountEntity) {
        return new ResponseEntity<>(accountService.updateFullNameAndPhoneNo(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/account-by-admin", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Integer> updateAccountByAdmin(@RequestBody AccountEntity accountEntity) {
        try {
            return new ResponseEntity<>(accountService.updateFullNameAndPhoneNoByAdmin(accountEntity), HttpStatus.OK);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Cannot send email.");
        }
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<AccountEntity>> getAllAccount() {
        List<AccountEntity> lst = accountService.getAllAccount();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(AccountEntity::getId));
        }
        return new ResponseEntity<List<AccountEntity>>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts-paging", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ListAccountModel> getAllAccountWithPaging(@RequestParam(value = "numOfElement") Integer numOfElement,
                                                                   @RequestParam(value = "page") Integer page,
                                                                   @RequestParam(value = "sort") String sort,
                                                                   @RequestParam(value = "status") String status,
                                                                   @RequestParam(value = "name") String name,
                                                                   @RequestParam(value = "role") String role) {
        Pageable pageable = CommonUtils.configPageable(numOfElement, page, sort);
        return new ResponseEntity<>(accountService.getAllAccountWithPaging(pageable, name, status, role), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<AccountEntity> getAccountById(@PathVariable("id") int id) {
        return new ResponseEntity<AccountEntity>(accountService.findAccountEntityById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account-by-email", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<AccountEntity> getAccountByEmail(@RequestParam("email") String email, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<AccountEntity>(accountService.findAccountByEmail(email), HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(confirmationToken);
        if (token != null) {
            AccountEntity user = accountService.findAccountByEmail(token.getUser().getEmail());
            user.setConfirmMail(true);
            accountService.updateAccount(user);
            return "<HTML><body> <a href=\"http://localhost:9527/#\">Confirm Successful (Click to go back)</a></body></HTML>";
        } else {
            return "<HTML><body> <a href=\"http://localhost:9527/#\">Confirm Error (Click to go back)</a></body></HTML>";
        }
    }

    @RequestMapping(value = "/reset-password/{email}", method = RequestMethod.POST)
    public ResponseEntity<AccountEntity> resetPassword(@PathVariable("email") String email) throws MessagingException {
        AccountEntity accountEntity = accountService.findAccountByEmail(email);
        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not exist");
        }
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random(15, characters);
        accountEntity.setPassword(passwordEncoder.encode(pwd));
        this.sendMail(email, "Reset Password!", resetPasswordMail(pwd));
        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
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
