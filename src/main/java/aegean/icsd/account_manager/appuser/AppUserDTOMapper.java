package aegean.icsd.account_manager.appuser;

import org.springframework.stereotype.Component;

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
