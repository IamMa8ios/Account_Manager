package aegean.icsd.account_manager.appuser;

import aegean.icsd.account_manager.appuser.dto.SelectAppUserRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> loadProfile(@RequestBody SelectAppUserRequest selectAppUserRequest, HttpSession session){

        this.appUserService.loadAppUserProfile(selectAppUserRequest.username(), session);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
