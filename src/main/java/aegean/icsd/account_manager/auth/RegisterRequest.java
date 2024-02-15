package aegean.icsd.account_manager.auth;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String email, @NotBlank String password) {
}
