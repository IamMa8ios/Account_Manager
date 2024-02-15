package aegean.icsd.account_manager.auth;

import aegean.icsd.account_manager.entities.Account;
import aegean.icsd.account_manager.user.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    public AuthService(AccountRepository accountRepository, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    public Integer registerUser(RegisterRequest request){

        if (this.accountRepository.existsByEmail(request.email())){
            System.out.println("Email already in use");
            return null;
        }

        Account user=new Account(request.firstName(), request.lastName(), request.email(), encoder.encode(request.password()));
        user=this.accountRepository.save(user);

        return user.getId();
    }

    public void loginUser(LoginRequest request){

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            System.out.println("Welcome, "+request.email());
        }catch (BadCredentialsException bce){
            System.out.println(bce.getMessage());
            throw bce;
        }

    }

}
