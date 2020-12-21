package capstone.oras.api.account.service;

import capstone.oras.entity.AccountEntity;
import capstone.oras.model.custom.ListAccountModel;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;

public interface IAccountService {
    String login(String email, String password);
    AccountEntity createAccount(AccountEntity accountEntity);
    AccountEntity updateAccount(AccountEntity accountEntity);
    List<AccountEntity> getAllAccount();
    ListAccountModel getAllAccountWithPaging(Pageable pageable, String name, String status, String role);
    AccountEntity findAccountByEmail(String email);
    AccountEntity findAccountEntityById(int id);
    AccountEntity findAccountByCompanyId(int id);
    Integer updateFullNameAndPhoneNo(AccountEntity accountEntity);
    Integer updateFullNameAndPhoneNoByAdmin(AccountEntity accountEntity) throws MessagingException;
    Integer updatePassword(AccountEntity accountEntity);
    AccountEntity createAccountByAdmin(AccountEntity accountEntity);
}
