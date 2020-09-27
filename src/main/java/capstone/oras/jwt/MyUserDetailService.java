package capstone.oras.jwt;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        AccountEntity account = accountRepository.findAccountEntitiesByEmailEquals(email);
        if (account == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserDetail(account);
    }

    public UserDetail loadUserById(int id) {
        return new UserDetail(accountRepository.findAccountEntitiesById(id));
    }
}
