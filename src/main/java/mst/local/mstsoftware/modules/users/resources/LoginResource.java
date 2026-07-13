package mst.local.mstsoftware.modules.users.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResource {
    private final String accessToken;
    private final UserResource user;
}
