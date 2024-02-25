package aegean.icsd.account_manager.appuser.dto;

import aegean.icsd.account_manager.account.Account;
import aegean.icsd.account_manager.appuser.Role;

public record CreateUserRequest(Account account, String username, Role role) {
}
