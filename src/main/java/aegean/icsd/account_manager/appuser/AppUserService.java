package aegean.icsd.account_manager.appuser;

import aegean.icsd.account_manager.appuser.dto.CreateUserRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public void createAppUser(CreateUserRequest request){

        AppUser appUser = new AppUser(request.account(), request.username(), request.role());
        this.appUserRepository.save(appUser);

    }


    //TODO: Add in DB actual profile data to be loaded
    public void loadAppUserProfile(String username, HttpSession session){
        AppUser appUser = this.appUserRepository.findAppUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("User found");

        session.setAttribute("Username", appUser.getUsername());
    }

}
