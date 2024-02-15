package aegean.icsd.account_manager.appuser;

import aegean.icsd.account_manager.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    public boolean existsByEmail(String email);

    public Optional<Account> findByEmail(String email);

}
