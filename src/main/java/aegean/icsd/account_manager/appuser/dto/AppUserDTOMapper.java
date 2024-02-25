package aegean.icsd.account_manager.appuser.dto;

import aegean.icsd.account_manager.appuser.AppUser;
import aegean.icsd.account_manager.appuser.dto.AppUserDTO;

import java.util.function.Function;

public class AppUserDTOMapper implements Function<AppUser, AppUserDTO> {

    @Override
    public AppUserDTO apply(AppUser appUser) {
        return new AppUserDTO(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getRole()
        );
    }

}
