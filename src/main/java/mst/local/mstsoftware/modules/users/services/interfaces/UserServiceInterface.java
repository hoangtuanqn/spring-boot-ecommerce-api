package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.dtos.LoginRequest;
import mst.local.mstsoftware.modules.users.dtos.LoginResponse;

public interface UserServiceInterface {
    LoginResponse login(LoginRequest request);
}
