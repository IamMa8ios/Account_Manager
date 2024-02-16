package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.account.Account;
import aegean.icsd.account_manager.account.AccountRepository;
import aegean.icsd.account_manager.appuser.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AuthService {

    private final AccountRepository accountRepository;

    private final AppUserService appUserService;

    private final PasswordEncoder encoder;

    @Autowired
    private EntityManager entityManager;

    private final AppUserDTOMapper dtoMapper;

    public AuthService(AccountRepository accountRepository, AppUserService appUserService, PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.appUserService = appUserService;
        this.encoder = encoder;
        this.dtoMapper = new AppUserDTOMapper();
    }


    @Transactional
    AuthResponse registerUser(RegisterRequest request) {

        if (this.accountRepository.existsByEmail(request.email())) {
            System.out.println("Email already in use");
            return null;
        }

        Account account = new Account(request.firstName(), request.lastName(), request.email(), encoder.encode(request.password()));
        this.accountRepository.save(account);

        //create default app user
        String defaultUsername = request.email().split("@")[0];
        CreateUserRequest createUserRequest = new CreateUserRequest(account, defaultUsername, Role.ACCOUNT_OWNER);
        appUserService.createAppUser(createUserRequest);

        //can not return newly created account and app user in same transaction (hibernate magic)
        entityManager.refresh(account);

        //convert app users to dto
        Set<AppUserDTO> appUserDTOS=account .getAppUsers()
                                            .stream()
                                            .map(this.dtoMapper)
                                            .collect(Collectors.toSet());

        return new AuthResponse(appUserDTOS);
    }

    public AuthResponse loginUser(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("Username or password incorrect"));

        //password match check (using same salt)
        if (!this.encoder.matches(request.password(), account.getPassword())) throw new RuntimeException("Username or password incorrect");

        Set<AppUserDTO> appUserDTOS=account .getAppUsers()
                                            .stream()
                                            .map(this.dtoMapper)
                                            .collect(Collectors.toSet());

        return new AuthResponse(appUserDTOS);

    }

}
