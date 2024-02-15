package aegean.icsd.account_manager.entities;


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
