package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.AuthResult;

import java.util.Optional;

public interface UserServiceInterface {
    AuthResult authenticate(LoginRequest request);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);
}
