package capstone.oras.account.service;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.jwt.JwtTokenProvider;
import capstone.oras.jwt.MyUserDetailService;
import capstone.oras.jwt.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public String login(String email, String password) {
        AccountEntity accountEntity = accountRepository.findAccountEntitiesByEmailEqualsAndPasswordEquals(email, password);
        if (accountEntity != null) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
            UserDetail userDetail = (UserDetail) userDetailService.loadUserByUsername(accountEntity.getEmail());
            return jwtTokenProvider.generateToken(userDetail);
        }
        System.out.println(accountEntity.getEmail());
        return null;
    }
}
