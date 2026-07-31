package mst.local.mstsoftware.modules.users.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email is required!")
        @Email(message = "Email invalid!")
        String email,

        @Size(min = 6, message = "Minimum of 6 character!")
        String password
) {
}
