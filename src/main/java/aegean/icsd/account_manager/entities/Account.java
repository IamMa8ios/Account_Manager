package aegean.icsd.account_manager.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = "accounts_email_key")
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    @OneToMany(mappedBy = "account")
    private Set<AppUser> appUsers;

    public Account(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }
}
