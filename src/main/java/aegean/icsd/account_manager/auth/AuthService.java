package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.account.Account;
import aegean.icsd.account_manager.account.AccountRepository;
import aegean.icsd.account_manager.appuser.*;
import aegean.icsd.account_manager.appuser.dto.AppUserDTO;
import aegean.icsd.account_manager.appuser.dto.AppUserDTOMapper;
import aegean.icsd.account_manager.appuser.dto.CreateUserRequest;
import aegean.icsd.account_manager.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

    private final AuthenticationManager authenticationManager;

    private final AppUserDTOMapper dtoMapper;

    public AuthService(AccountRepository accountRepository, AppUserService appUserService, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.appUserService = appUserService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.dtoMapper = new AppUserDTOMapper();
    }


    @Transactional
    AuthResponse registerUser(RegisterRequest request, HttpSession session) {

        if (this.accountRepository.existsByEmail(request.email())) {
            System.out.println("Email already in use");
            return null;
        }

        Account account = new Account(request.firstName(), request.lastName(), request.email(), encoder.encode(request.password()));
        this.accountRepository.save(account);

        //create default app account
        String defaultUsername = request.email().split("@")[0];
        CreateUserRequest createUserRequest = new CreateUserRequest(account, defaultUsername, Role.ACCOUNT_OWNER);
        appUserService.createAppUser(createUserRequest);

        //can not return newly created account and app account in same transaction (hibernate magic)
        entityManager.refresh(account);

        setContext(account, session);

        //convert app users to dto
        Set<AppUserDTO> appUserDTOS=account .getAppUsers()
                                            .stream()
                                            .map(this.dtoMapper)
                                            .collect(Collectors.toSet());

        return new AuthResponse(appUserDTOS);
    }

    public AuthResponse loginUser(LoginRequest request, HttpSession session) {

        Authentication authentication;

        try {
            authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        }catch (BadCredentialsException bce){
            System.out.println("Wrong email or password");
            throw new RuntimeException();
        }

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        setContext(securityUser.account(), session);

        Set<AppUserDTO> appUserDTOS=securityUser.account()
                                    .getAppUsers()
                                    .stream()
                                    .map(this.dtoMapper)
                                    .collect(Collectors.toSet());

        return new AuthResponse(appUserDTOS);

    }

    private void setContext(Account account, HttpSession session) {
        SecurityUser securityUser = new SecurityUser(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    //TODO: Custom Exceptions & Handling
    //TODO: Request Validation

}
