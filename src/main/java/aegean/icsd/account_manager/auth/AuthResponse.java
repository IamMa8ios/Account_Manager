package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.appuser.dto.AppUserDTO;

import java.util.Set;

public record AuthResponse(Set<AppUserDTO> appUsers) {

}
