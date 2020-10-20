package capstone.oras.account.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        accountEntity.setActive(true);
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        return IAccountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity updateAccount(AccountEntity accountEntity) {
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        return IAccountRepository.save(accountEntity);
    }

    @Override
    public List<AccountEntity> getAllAccount() {
        return IAccountRepository.findAll();
    }

    @Override
    public AccountEntity findAccountByEmail(String email) {
        return IAccountRepository.findAccountEntitiesByEmailEquals(email).get();
    }

    @Override
    public AccountEntity findAccountEntityById(int id) {
        return IAccountRepository.findById(id).get();
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
