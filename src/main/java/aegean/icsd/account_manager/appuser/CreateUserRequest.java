package aegean.icsd.account_manager.appuser;

import aegean.icsd.account_manager.account.Account;

public record CreateUserRequest(Account account, String username, Role role) {
}
