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
import java.util.List;

import static capstone.oras.common.Constant.EmailForm.updateAccountNoti;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private IAccountRepository IAccountRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public AccountService(IAccountRepository IAccountRepository) {
        this.IAccountRepository = IAccountRepository;
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
        return IAccountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity updateAccount(AccountEntity accountEntity) {
//        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        return IAccountRepository.save(accountEntity);
    }

    @Override
    public List<AccountEntity> getAllAccount() {
        return IAccountRepository.findAll();
    }

    @Override
    public ListAccountModel getAllAccountWithPaging(Pageable pageable, String name, String status, String role) {
        List<AccountEntity> data;
        int count;
        name = "%" + name + "%";
        role = StringUtils.isEmpty(role) ? "%" : role;
        if (StringUtils.isEmpty(status)) {
            data = IAccountRepository.findAllByFullnameIgnoreCaseLikeAndRoleLike(pageable, name, role);
            count = IAccountRepository.countByFullnameIgnoreCaseLikeAndRoleLike(name, role);
        } else {
            data = IAccountRepository.findAllByFullnameIgnoreCaseLikeAndActiveIsAndRoleLike(pageable, name, "Active".equalsIgnoreCase(status), role);
            count = IAccountRepository.countByFullnameIgnoreCaseLikeAndActiveIsAndRoleLike(name, "Active".equalsIgnoreCase(status), role);
        }
        return new ListAccountModel(count, data);
    }

    @Override
    public AccountEntity findAccountByEmail(String email) {
        if (IAccountRepository.findAccountEntitiesByEmailEquals(email).isPresent()) {
            return IAccountRepository.findAccountEntitiesByEmailEquals(email).get();
        } else return null;
    }

    @Override
    public AccountEntity findAccountEntityById(int id) {
        if (IAccountRepository.findById(id).isPresent()) {
            return IAccountRepository.findById(id).get();
        } else return null;
    }

    @Override
    public AccountEntity findAccountByCompanyId(int id) {
        if (IAccountRepository.findAccountEntityByCompanyIdEquals(id).isPresent()) {
            return IAccountRepository.findAccountEntityByCompanyIdEquals(id).get();
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
            ret = IAccountRepository.updateFullNameAndPhoneNo(id, fullName, phoneNo);
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
            ret = IAccountRepository.updateFullNameAndPhoneNo(id, fullName, phoneNo);
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
//        return IAccountRepository.save(accountEntity);
//    }

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
