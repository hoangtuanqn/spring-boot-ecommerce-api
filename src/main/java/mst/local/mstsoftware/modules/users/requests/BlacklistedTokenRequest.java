package mst.local.mstsoftware.modules.users.requests;

import jakarta.validation.constraints.NotBlank;

public record BlacklistedTokenRequest(
        @NotBlank(message = "Token không được để trống")
        String token
) {
}
