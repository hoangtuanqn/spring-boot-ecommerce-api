package mst.local.mstsoftware.modules.user.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AssignRoleRequest {
    @NotNull(message = "userId không được để trống")
    private Long userId;

    @NotBlank(message = "roleName không được để trống")
    private String roleName;

    private Instant expiresAt; // null = vĩnh viễn
}
