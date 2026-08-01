package mst.local.mstsoftware.modules.user.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource {
    private final Long id;
    private final String email;
    private final String name;
    private final String phone;

    public UserResource(Long id, String email) {
        this(id, email, null, null);
    }
}
