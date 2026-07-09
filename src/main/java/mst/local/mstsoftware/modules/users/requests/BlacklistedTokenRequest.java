package mst.local.mstsoftware.modules.users.requests;

import jakarta.validation.constraints.NotBlank;

public class BlacklistedTokenRequest {

    @NotBlank(message = "Token không được để trống")
    private String token;
}
