package capstone.oras.dao;

import capstone.oras.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    ConfirmationToken findConfirmationTokenByConfirmationToken(String confirmationToken);
}
