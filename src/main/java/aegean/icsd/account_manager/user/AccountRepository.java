package aegean.icsd.account_manager.user;

import aegean.icsd.account_manager.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    public boolean existsByEmail(String email);

    public Optional<Account> findByEmail(String email);

}
