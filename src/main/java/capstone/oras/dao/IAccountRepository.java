package capstone.oras.dao;

import capstone.oras.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findAccountEntitiesByEmailEquals(String email);
    AccountEntity findAccountEntitiesById(int id);
    AccountEntity findAccountEntitiesByEmailEqualsAndPasswordEquals(String email, String password);
}
