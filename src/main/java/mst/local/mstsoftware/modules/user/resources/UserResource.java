package mst.local.mstsoftware.modules.user.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResource(Long id, String email, String name, String phone, Instant createdAt) {
}
