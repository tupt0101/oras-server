package capstone.oras.api.bulk_insert;


import capstone.oras.api.account.service.IAccountService;
import capstone.oras.api.company.service.ICompanyService;
import capstone.oras.api.email.service.EmailSenderService;
import capstone.oras.dao.IConfirmationTokenRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.entity.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/bulk")
public class BulkController {
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
    @Autowired
    private IBulkService bulkService;

    static class Signup {
        public AccountEntity accountEntity;
        public CompanyEntity companyEntity;
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> signup(@RequestBody List<Signup> signup) {
        int res = bulkService.signup(signup);
        return new ResponseEntity<>("Create " + res + "/" + signup.size() + " accounts", HttpStatus.OK);
    }
}
