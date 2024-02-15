package aegean.icsd.account_manager.appuser;

import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public void createAppUser(CreateUserRequest request){

        AppUser appUser = new AppUser(request.account(), request.username(), request.role());
        appUserRepository.save(appUser);

    }



}
