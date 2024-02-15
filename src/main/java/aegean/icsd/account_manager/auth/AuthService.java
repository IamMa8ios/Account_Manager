package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.account.Account;
import aegean.icsd.account_manager.appuser.*;
import aegean.icsd.account_manager.appuser.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final AccountRepository accountRepository;

    private final AppUserService appUserService;

    private final PasswordEncoder encoder;

    @Autowired
    private EntityManager entityManager;

    public AuthService(AccountRepository accountRepository, AppUserService appUserService, PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.appUserService = appUserService;
        this.encoder = encoder;
    }


    @Transactional
    AuthResponse registerUser(RegisterRequest request) {

        if (this.accountRepository.existsByEmail(request.email())) {
            System.out.println("Email already in use");
            return null;
        }

        Account account = new Account(request.firstName(), request.lastName(), request.email(), encoder.encode(request.password()));
        this.accountRepository.save(account);

        String defaultUsername = request.email().split("@")[0];
        CreateUserRequest createUserRequest = new CreateUserRequest(account, defaultUsername, Role.ACCOUNT_OWNER);
        appUserService.createAppUser(createUserRequest);

        entityManager.refresh(account);

        return new AuthResponse(account.getAppUsers());
    }

    public AuthResponse loginUser(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("Username or password incorrect"));

        if (!this.encoder.matches(request.password(), account.getPassword())) throw new RuntimeException("Username or password incorrect");

        return new AuthResponse(account.getAppUsers());

    }


}
