package mst.local.mstsoftware.modules.user.requests;

import jakarta.validation.constraints.NotBlank;

public record BlacklistedTokenRequest(
        @NotBlank(message = "Token không được để trống")
        String token
) {
}
