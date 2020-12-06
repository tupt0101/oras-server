package capstone.oras.api.account.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccountService implements IAccountService {

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private MyUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAccountRepository IAccountRepository;

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
    public List<AccountEntity> getAllAccountWithPaging(Pageable pageable) {
        return IAccountRepository.findAllBy(pageable);
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
        } else return null;    }

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
}
