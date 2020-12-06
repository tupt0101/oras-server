package capstone.oras.api.account.service;

import capstone.oras.entity.AccountEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {
    String login(String email, String password);
    AccountEntity createAccount(AccountEntity accountEntity);
    AccountEntity updateAccount(AccountEntity accountEntity);
    List<AccountEntity> getAllAccount();
    List<AccountEntity> getAllAccountWithPaging(Pageable pageable);
    AccountEntity findAccountByEmail(String email);
    AccountEntity findAccountEntityById(int id);
    AccountEntity findAccountByCompanyId(int id);
    Integer updateFullNameAndPhoneNo(AccountEntity accountEntity);
}
