package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.appuser.AppUser;

import java.util.Set;

public record AuthResponse(Set<AppUser> appUsers) {

}
