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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RestController
@CrossOrigin(value = "http://localhost:9527")
@RequestMapping(value = "/v1/account-management")
public class AccountController {

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
    private ICompanyService companyService;

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
    ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity accountEntity) {
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
            accountEntity.setCreateDate(LocalDateTime.now());
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<AccountEntity> signup(@RequestBody Signup signup) throws MessagingException {
        System.out.println(signup);
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
        } else {

            signup.accountEntity.setCreateDate(LocalDateTime.now());
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
//            signup.companyEntity.setVerified(false);
            CompanyEntity companyEntity = companyService.createCompany(signup.companyEntity);
            signup.accountEntity.setCompanyId(companyEntity.getId());
//            signup.accountEntity.setActive(false);
            signup.accountEntity.setConfirmMail(false);
            signup.accountEntity.setPassword(passwordEncoder.encode(signup.accountEntity.getPassword()));
            AccountEntity accountEntity = accountService.createAccount(signup.accountEntity);
            ConfirmationToken confirmationToken = new ConfirmationToken(accountEntity);

            confirmationTokenRepository.save(confirmationToken);


            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("Complete Registration!");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(signup.accountEntity.getEmail());

            // use the true flag to indicate the text included is HTML
            helper.setText("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                    "    <style type=\"text/css\">\n" +
                    "        @media screen {\n" +
                    "            @font-face {\n" +
                    "                font-family: 'Lato';\n" +
                    "                font-style: normal;\n" +
                    "                font-weight: 400;\n" +
                    "                src: local('Lato Regular'), local('Lato-Regular'), url(https://fonts.gstatic.com/s/lato/v11/qIIYRU-oROkIk8vfvxw6QvesZW2xOQ-xsNqO47m55DA.woff) format('woff');\n" +
                    "            }\n" +
                    "\n" +
                    "            @font-face {\n" +
                    "                font-family: 'Lato';\n" +
                    "                font-style: normal;\n" +
                    "                font-weight: 700;\n" +
                    "                src: local('Lato Bold'), local('Lato-Bold'), url(https://fonts.gstatic.com/s/lato/v11/qdgUG4U09HnJwhYI-uK18wLUuEpTyoUstqEm5AMlJo4.woff) format('woff');\n" +
                    "            }\n" +
                    "\n" +
                    "            @font-face {\n" +
                    "                font-family: 'Lato';\n" +
                    "                font-style: italic;\n" +
                    "                font-weight: 400;\n" +
                    "                src: local('Lato Italic'), local('Lato-Italic'), url(https://fonts.gstatic.com/s/lato/v11/RYyZNoeFgb0l7W3Vu1aSWOvvDin1pK8aKteLpeZ5c0A.woff) format('woff');\n" +
                    "            }\n" +
                    "\n" +
                    "            @font-face {\n" +
                    "                font-family: 'Lato';\n" +
                    "                font-style: italic;\n" +
                    "                font-weight: 700;\n" +
                    "                src: local('Lato Bold Italic'), local('Lato-BoldItalic'), url(https://fonts.gstatic.com/s/lato/v11/HkF_qI1x_noxlxhrhMQYELO3LdcAZYWl9Si6vvxL-qU.woff) format('woff');\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        /* CLIENT-SPECIFIC STYLES */\n" +
                    "        body,\n" +
                    "        table,\n" +
                    "        td,\n" +
                    "        a {\n" +
                    "            -webkit-text-size-adjust: 100%;\n" +
                    "            -ms-text-size-adjust: 100%;\n" +
                    "        }\n" +
                    "\n" +
                    "        table,\n" +
                    "        td {\n" +
                    "            mso-table-lspace: 0pt;\n" +
                    "            mso-table-rspace: 0pt;\n" +
                    "        }\n" +
                    "\n" +
                    "        img {\n" +
                    "            -ms-interpolation-mode: bicubic;\n" +
                    "        }\n" +
                    "\n" +
                    "        /* RESET STYLES */\n" +
                    "        img {\n" +
                    "            border: 0;\n" +
                    "            height: auto;\n" +
                    "            line-height: 100%;\n" +
                    "            outline: none;\n" +
                    "            text-decoration: none;\n" +
                    "        }\n" +
                    "\n" +
                    "        table {\n" +
                    "            border-collapse: collapse !important;\n" +
                    "        }\n" +
                    "\n" +
                    "        body {\n" +
                    "            height: 100% !important;\n" +
                    "            margin: 0 !important;\n" +
                    "            padding: 0 !important;\n" +
                    "            width: 100% !important;\n" +
                    "        }\n" +
                    "\n" +
                    "        /* iOS BLUE LINKS */\n" +
                    "        a[x-apple-data-detectors] {\n" +
                    "            color: inherit !important;\n" +
                    "            text-decoration: none !important;\n" +
                    "            font-size: inherit !important;\n" +
                    "            font-family: inherit !important;\n" +
                    "            font-weight: inherit !important;\n" +
                    "            line-height: inherit !important;\n" +
                    "        }\n" +
                    "\n" +
                    "        /* MOBILE STYLES */\n" +
                    "        @media screen and (max-width:600px) {\n" +
                    "            h1 {\n" +
                    "                font-size: 32px !important;\n" +
                    "                line-height: 32px !important;\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        /* ANDROID CENTER FIX */\n" +
                    "        div[style*=\"margin: 16px 0;\"] {\n" +
                    "            margin: 0 !important;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"background-color: #e1e1e1; margin: 0 !important; padding: 0 !important;\">\n" +
                    "    <!-- HIDDEN PREHEADER TEXT -->\n" +
                    "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Lato', Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\"> We're thrilled to have you here! Get ready to dive into your new account. </div>\n" +
                    "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "        <!-- LOGO -->\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#1746e0\" align=\"center\">\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#1746e0\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\n" +
                    "                            <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">Welcome!</h1> <img src=\" https://img.icons8.com/clouds/100/000000/handshake.png\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" />\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td bgcolor=\"#e1e1e1\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                    "                            <p style=\"margin: 0;\">We're excited to have you get started. First, you need to confirm your account. Just press the button below.</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\">\n" +
                    "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                <tr>\n" +
                    "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n" +
                    "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#1746e0\"><a href=\"https://oras-api.herokuapp.com/v1/account-management/confirm-account?token=" + confirmationToken.getConfirmationToken() + "\" target=\"_blank\" style=\"font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #1746e0; display: inline-block;\">Confirm Account</a></td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                        </td>\n" +
                    "                    </tr> <!-- COPY -->\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 0px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                    "                            <p style=\"margin: 0;\">If that doesn't work, copy and paste the following link in your browser:</p>\n" +
                    "                        </td>\n" +
                    "                    </tr> <!-- COPY -->\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                    "                            <p style=\"margin: 0;\"><a href=\"#\" target=\"_blank\" style=\"color: #1746e0;\">https://oras-api.herokuapp.com/v1/account-management/confirm-account?token=" + confirmationToken.getConfirmationToken() + "</a></p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                    "                            <p style=\"margin: 0;\">If you have any questions, just reply to this email—we're always happy to help out.</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                    "                            <p style=\"margin: 0;\">Cheers,<br>ORAS Team</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>", true);
            javaMailSender.send(message);
            return new ResponseEntity<>(accountService.createAccount(accountEntity), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/activate-account/{id}", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<AccountEntity> activeAccountViaCompany(@PathVariable("id") int companyId) {

        if (companyId == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company Id is a required field");
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
    ResponseEntity<AccountEntity> changePassword(@RequestBody PasswordChanges passwordChanges) {
        AccountEntity accountEntity = accountService.findAccountEntityById(passwordChanges.accountId);
        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        }else if (!passwordEncoder.matches(passwordChanges.currentPassword, accountEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password doesn't match current password");
        }
        accountEntity.setPassword(passwordEncoder.encode(passwordChanges.newPassword));


        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/update-account", method = RequestMethod.PUT)
    @ResponseBody
    ResponseEntity<Integer> customUpdateAccount(@RequestBody AccountEntity accountEntity) {
        return new ResponseEntity<>(accountService.updateFullNameAndPhoneNo(accountEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountEntity>> getAllAccount() {
        List<AccountEntity> lst = accountService.getAllAccount();
        if (!CollectionUtils.isEmpty(lst)) {
            lst.sort(Comparator.comparingInt(AccountEntity::getId));
        }
        return new ResponseEntity<List<AccountEntity>>(lst, HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts-paging", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<AccountEntity>> getAllAccountWithPaging(@RequestParam(value = "numOfElement") int numOfElement, @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page - 1, numOfElement, Sort.by("createDate").descending());
        return new ResponseEntity<List<AccountEntity>>(accountService.getAllAccountWithPaging(pageable), HttpStatus.OK);
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
        }else if (!accountEntity.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account has not been activated");
        }
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random( 15, characters );
        accountEntity.setPassword(passwordEncoder.encode(pwd));
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("Reset Password!");
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);

        // use the true flag to indicate the text included is HTML
        helper.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        @media screen {\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Lato Regular'), local('Lato-Regular'), url(https://fonts.gstatic.com/s/lato/v11/qIIYRU-oROkIk8vfvxw6QvesZW2xOQ-xsNqO47m55DA.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: normal;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Lato Bold'), local('Lato-Bold'), url(https://fonts.gstatic.com/s/lato/v11/qdgUG4U09HnJwhYI-uK18wLUuEpTyoUstqEm5AMlJo4.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: italic;\n" +
                "                font-weight: 400;\n" +
                "                src: local('Lato Italic'), local('Lato-Italic'), url(https://fonts.gstatic.com/s/lato/v11/RYyZNoeFgb0l7W3Vu1aSWOvvDin1pK8aKteLpeZ5c0A.woff) format('woff');\n" +
                "            }\n" +
                "\n" +
                "            @font-face {\n" +
                "                font-family: 'Lato';\n" +
                "                font-style: italic;\n" +
                "                font-weight: 700;\n" +
                "                src: local('Lato Bold Italic'), local('Lato-BoldItalic'), url(https://fonts.gstatic.com/s/lato/v11/HkF_qI1x_noxlxhrhMQYELO3LdcAZYWl9Si6vvxL-qU.woff) format('woff');\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        /* CLIENT-SPECIFIC STYLES */\n" +
                "        body,\n" +
                "        table,\n" +
                "        td,\n" +
                "        a {\n" +
                "            -webkit-text-size-adjust: 100%;\n" +
                "            -ms-text-size-adjust: 100%;\n" +
                "        }\n" +
                "\n" +
                "        table,\n" +
                "        td {\n" +
                "            mso-table-lspace: 0pt;\n" +
                "            mso-table-rspace: 0pt;\n" +
                "        }\n" +
                "\n" +
                "        img {\n" +
                "            -ms-interpolation-mode: bicubic;\n" +
                "        }\n" +
                "\n" +
                "        /* RESET STYLES */\n" +
                "        img {\n" +
                "            border: 0;\n" +
                "            height: auto;\n" +
                "            line-height: 100%;\n" +
                "            outline: none;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            border-collapse: collapse !important;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            height: 100% !important;\n" +
                "            margin: 0 !important;\n" +
                "            padding: 0 !important;\n" +
                "            width: 100% !important;\n" +
                "        }\n" +
                "\n" +
                "        /* iOS BLUE LINKS */\n" +
                "        a[x-apple-data-detectors] {\n" +
                "            color: inherit !important;\n" +
                "            text-decoration: none !important;\n" +
                "            font-size: inherit !important;\n" +
                "            font-family: inherit !important;\n" +
                "            font-weight: inherit !important;\n" +
                "            line-height: inherit !important;\n" +
                "        }\n" +
                "\n" +
                "        /* MOBILE STYLES */\n" +
                "        @media screen and (max-width:600px) {\n" +
                "            h1 {\n" +
                "                font-size: 32px !important;\n" +
                "                line-height: 32px !important;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        /* ANDROID CENTER FIX */\n" +
                "        div[style*=\"margin: 16px 0;\"] {\n" +
                "            margin: 0 !important;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #e1e1e1; margin: 0 !important; padding: 0 !important;\">\n" +
                "    <!-- HIDDEN PREHEADER TEXT -->\n" +
                "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Lato', Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\"> We're thrilled to have you here! Get ready to dive into your new account. </div>\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <!-- LOGO -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#1746e0\" align=\"center\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#1746e0\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\n" +
                "                            <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">Reset Password!</h1> <img src=\" https://img.icons8.com/clouds/100/000000/handshake.png\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" />\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#e1e1e1\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">You've successfully reset your ORAS password.</p>\n" +
                "                            <p style=\"margin: 0;\">Your new password: <strong>"+ pwd +"</strong></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\">\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n" +
                "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr> <!-- COPY -->\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">If you have any questions, just reply to this email—we're always happy to help out.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n" +
                "                            <p style=\"margin: 0;\">Cheers,<br>ORAS Team</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>", true);
        javaMailSender.send(message);
        return new ResponseEntity<>(accountService.updateAccount(accountEntity), HttpStatus.OK);
    }


}
