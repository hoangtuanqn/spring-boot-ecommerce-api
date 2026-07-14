package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.AuthResult;

public interface UserServiceInterface {
    AuthResult authenticate(LoginRequest request);
}
