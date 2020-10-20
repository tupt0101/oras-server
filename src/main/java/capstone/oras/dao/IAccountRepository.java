package capstone.oras.dao;

import capstone.oras.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findAccountEntitiesByEmailEquals(String email);
    AccountEntity findAccountEntitiesById(int id);
    AccountEntity findAccountEntitiesByEmailEqualsAndPasswordEquals(String email, String password);
}
