package aegean.icsd.account_manager.appuser;

import aegean.icsd.account_manager.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Set<AppUser> findAppUsersByAccount(Account account);

}
