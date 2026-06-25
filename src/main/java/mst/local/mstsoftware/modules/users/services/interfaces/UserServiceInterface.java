package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.LoginResource;

public interface UserServiceInterface {
    LoginResource authenticate(LoginRequest request);
}
