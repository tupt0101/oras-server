package capstone.oras.account.service;

import capstone.oras.entity.AccountEntity;

import java.util.List;

public interface IAccountService {
    String login(String email, String password);
    AccountEntity createAccount(AccountEntity accountEntity);
    AccountEntity updateAccount(AccountEntity accountEntity);
    List<AccountEntity> getAllAccount();
}
