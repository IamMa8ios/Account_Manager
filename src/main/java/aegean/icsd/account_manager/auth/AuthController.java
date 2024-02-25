package aegean.icsd.account_manager.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session){

        AuthResponse availableUsers=this.authService.registerUser(request, session);

        return new ResponseEntity<>(availableUsers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session){

        AuthResponse availableUsers=this.authService.loginUser(request, session);

        return new ResponseEntity<>(availableUsers, HttpStatus.OK);
    }

    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> getCSRF(CsrfToken csrfToken){
        return new ResponseEntity<>(csrfToken, HttpStatus.OK);
    }

}
