package aegean.icsd.account_manager.appuser;


import aegean.icsd.account_manager.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "app_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = "accounts_email_key")
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    @ManyToOne
    private Account account;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;

    public AppUser(Account account, String username, Role role) {
        this.account = account;
        this.username = username;
        this.role = role;
    }

}
