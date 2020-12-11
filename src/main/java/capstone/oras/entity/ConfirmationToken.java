package capstone.oras.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static capstone.oras.common.Constant.TIME_ZONE;

@Getter
@Setter
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private long tokenid;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Column(name="create_date")
    private LocalDateTime createDate;

    @OneToOne(targetEntity = AccountEntity.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_id")
    private AccountEntity user;

    public ConfirmationToken() {
    }

    public ConfirmationToken(AccountEntity user) {
        this.user = user;
        this.createDate = LocalDateTime.now(TIME_ZONE);
        confirmationToken = UUID.randomUUID().toString();
    }
}
