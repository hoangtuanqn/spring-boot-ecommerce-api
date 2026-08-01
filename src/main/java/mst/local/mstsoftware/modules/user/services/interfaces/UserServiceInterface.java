package mst.local.mstsoftware.modules.user.services.interfaces;

import java.util.Optional;

import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.requests.LoginRequest;
import mst.local.mstsoftware.modules.user.resources.AuthResult;

public interface UserServiceInterface {
    AuthResult authenticate(LoginRequest request);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);
}
