package capstone.oras.account.service;

import capstone.oras.entity.AccountEntity;

public interface IAccountService {
    String login(String email, String password);
    AccountEntity createAccount(String email, String password,String fullname);
}
