package capstone.oras.api.account.controller;

import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.email.service.EmailSenderService;
import capstone.oras.dao.IConfirmationTokenRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import capstone.oras.entity.ConfirmationToken;
import capstone.oras.entity.openjob.OpenjobCompanyEntity;
import capstone.oras.oauth2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/account-management")
public class AccountController {

    @Autowired
    IConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICompanyService companyService;

    HttpHeaders httpHeaders = new HttpHeaders();

    static class Signup {
        public AccountEntity accountEntity;
        public CompanyEntity companyEntity;
    }


    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity accountEntity) {
        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is empty");
        } else if (accountEntity.getPassword() == null || accountEntity.getPassword().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is empty");
        } else if (accountService.findAccountByEmail(accountEntity.getEmail()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountService.findAccountEntityById(accountEntity.getId()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        } else {
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountEntity> signup(@RequestBody Signup signup) {
        System.out.println(signup);
        if (signup.accountEntity.getEmail() == null || signup.accountEntity.getEmail().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        } else if (signup.accountEntity.getFullname() == null || signup.accountEntity.getFullname().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is empty");
        } else if (signup.accountEntity.getPassword() == null || signup.accountEntity.getPassword().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is empty");
        } else if (accountService.findAccountByEmail(signup.accountEntity.getEmail()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountService.findAccountEntityById(signup.accountEntity.getId()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exist");
        } else {


            //get openjob token
            CustomUserDetailsService userDetailsService = new CustomUserDetailsService();
            String token = "Bearer " + userDetailsService.getOpenJobToken();
            // post company to openjob
            String uri = "https://openjob-server.herokuapp.com/v1/company-management/company-by-name/" + signup.companyEntity.getName();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
//        headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity(headers);
            CompanyEntity openJobEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, CompanyEntity.class).getBody();
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
                openjobCompanyEntity = restTemplate.postForObject(uri, httpCompanyEntity, OpenjobCompanyEntity.class);
                signup.companyEntity.setOpenjobCompanyId(openjobCompanyEntity.getId());
            } else {
                signup.companyEntity.setOpenjobCompanyId(openJobEntity.getId());
            }
            signup.companyEntity.setVerified(false);
            CompanyEntity companyEntity = companyService.createCompany(signup.companyEntity);
            signup.accountEntity.setCompanyId(companyEntity.getId());
            signup.accountEntity.setActive(false);
            signup.accountEntity.setConfirmMail(false);
            ConfirmationToken confirmationToken = new ConfirmationToken(signup.accountEntity);

            confirmationTokenRepository.save(confirmationToken);
            // send email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(signup.accountEntity.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("chand312902@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/v1/account-management/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
            return new ResponseEntity<>(accountService.createAccount(signup.accountEntity), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/activate-account/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountEntity> activeAccountViaCompany(@PathVariable("id") int companyId) {

        if (companyId == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id is empty");
        } else if (companyService.findCompanyById(companyId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company doesn't exist");
        }
        // refactor code de update 1 field thoi dung native query nang cao hieu suat
        CompanyEntity companyEntity = companyService.findCompanyById(companyId);
        companyEntity.setVerified(true);
        companyService.updateCompany(companyEntity);
        AccountEntity accountEntity = accountService.findAccountByCompanyId(companyId);
        accountEntity.setActive(true);
        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }


    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountEntity> updateAccount(@RequestBody AccountEntity accountEntity) {

        if (accountEntity.getEmail() == null || accountEntity.getEmail().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        } else if (accountEntity.getFullname() == null || accountEntity.getFullname().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is empty");
        } else if (accountEntity.getPassword() == null || accountEntity.getPassword().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is empty");
        } else if (accountService.findAccountByEmail(accountEntity.getEmail()) != null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already registered");
        } else if (accountService.findAccountEntityById(accountEntity.getId()) == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        } else {
            return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountEntity>> getAllAccount() {
        List<AccountEntity> lst = accountService.getAllAccount();
        lst.sort(Comparator.comparingInt(AccountEntity::getId));
        return new ResponseEntity<List<AccountEntity>>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountEntity> getAccountById(@PathVariable("id") int id) {
        return new ResponseEntity<AccountEntity>(accountService.findAccountEntityById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account-by-email", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<AccountEntity> getAccountByEmail(@RequestParam("email") String email, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<AccountEntity>(accountService.findAccountByEmail(email), HttpStatus.OK);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findConfirmationTokenByConfirmationToken(confirmationToken);

        if(token != null)
        {
            AccountEntity user = accountService.findAccountByEmail(token.getUser().getEmail());
            user.setConfirmMail(true);
            accountService.updateAccount(user);
            return "redirect:http://localhost:8080//confirm_success";
        } else {
            return "redirect:http://localhost:8080//confirm_error";
        }
    }
}
