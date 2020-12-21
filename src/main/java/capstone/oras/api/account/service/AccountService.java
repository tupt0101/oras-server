package capstone.oras.api.account.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.model.custom.ListAccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

import static capstone.oras.common.Constant.EmailForm.updateAccountNoti;
import static capstone.oras.common.Constant.TIME_ZONE;

@Service
public class AccountService implements IAccountService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public AccountService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    public AccountService(PasswordEncoder passwordEncoder, IAccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    public String login(String email, String password) {
//        AccountEntity accountEntity = accountRepository.findAccountEntitiesByEmailEqualsAndPasswordEquals(email, password);
//        if (accountEntity != null) {
////            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
//            User userDetail = (User) userDetailService.loadUserByUsername(accountEntity.getEmail());
//            return jwtTokenProvider.generateToken(userDetail);
//        }
//        System.out.println(accountEntity.getEmail());
        return null;
    }

    @Override
    public AccountEntity createAccount(AccountEntity accountEntity) {
//        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        return accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity updateAccount(AccountEntity accountEntity) {
//        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        return accountRepository.save(accountEntity);
    }

    @Override
    public List<AccountEntity> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public ListAccountModel getAllAccountWithPaging(Pageable pageable, String name, String status, String role) {
        List<AccountEntity> data;
        int count;
        name = "%" + name + "%";
        role = StringUtils.isEmpty(role) ? "%" : role;
        if (StringUtils.isEmpty(status)) {
            data = accountRepository.findAllByFullnameIgnoreCaseLikeAndRoleLike(pageable, name, role);
            count = accountRepository.countByFullnameIgnoreCaseLikeAndRoleLike(name, role);
        } else {
            data = accountRepository.findAllByFullnameIgnoreCaseLikeAndActiveIsAndRoleLike(pageable, name, "Active".equalsIgnoreCase(status), role);
            count = accountRepository.countByFullnameIgnoreCaseLikeAndActiveIsAndRoleLike(name, "Active".equalsIgnoreCase(status), role);
        }
        return new ListAccountModel(count, data);
    }

    @Override
    public AccountEntity findAccountByEmail(String email) {
        return accountRepository.findAccountEntitiesByEmailEquals(email).orElse(null);
    }

    @Override
    public AccountEntity findAccountEntityById(int id) {
        if (accountRepository.findById(id).isPresent()) {
            return accountRepository.findById(id).get();
        } else return null;
    }

    @Override
    public AccountEntity findAccountByCompanyId(int id) {
        if (accountRepository.findAccountEntityByCompanyIdEquals(id).isPresent()) {
            return accountRepository.findAccountEntityByCompanyIdEquals(id).get();
        } else return null;
    }

    @Override
    public Integer updateFullNameAndPhoneNo(AccountEntity accountEntity) {
        Integer id = accountEntity.getId();
        String fullName = accountEntity.getFullname();
        String phoneNo = accountEntity.getPhoneNo();
        if (this.findAccountEntityById(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        }
        if (StringUtils.isEmpty(fullName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        }
        if (StringUtils.isEmpty(phoneNo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is a required field");
        }
        int ret;
        try {
            ret = accountRepository.updateFullNameAndPhoneNo(id, fullName, phoneNo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ret;
    }

    @Override
    public Integer updateFullNameAndPhoneNoByAdmin(AccountEntity accountEntity) throws MessagingException {
        String fullName = accountEntity.getFullname();
        String phoneNo = accountEntity.getPhoneNo();
        int id = accountEntity.getId();
        AccountEntity raw = findAccountEntityById(id);
        if (raw == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account doesn't exist");
        }
        if (StringUtils.isEmpty(fullName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is a required field");
        }
        if (StringUtils.isEmpty(phoneNo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is a required field");
        }
        int ret;
        try {
            ret = accountRepository.updateFullNameAndPhoneNo(id, fullName, phoneNo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        this.sendMail(raw.getEmail(), "Your personal information has been changed", updateAccountNoti(fullName, phoneNo, raw.getFullname(), raw.getPhoneNo()));
        return ret;
    }

    @Override
    public Integer updatePassword(AccountEntity accountEntity) {
        return null;
    }

//    @Override
//    public AccountEntity createAccount(String email, String password,String fullname) {
//        AccountEntity accountEntity = new AccountEntity();
//        accountEntity.setActive(true);
//        accountEntity.setEmail(email);
//        accountEntity.setFullname(fullname);
//        return accountRepository.save(accountEntity);
//    }

    @Override
    public AccountEntity createAccountByAdmin(AccountEntity accountEntity) {
        if (this.findAccountByEmail(accountEntity.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already exists.");
        }
        accountEntity.setActive(true);
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        accountEntity.setRole("Admin");
        accountEntity.setConfirmMail(true);
        accountEntity.setCreateDate(LocalDateTime.now(TIME_ZONE));
        return accountRepository.save(accountEntity);
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
