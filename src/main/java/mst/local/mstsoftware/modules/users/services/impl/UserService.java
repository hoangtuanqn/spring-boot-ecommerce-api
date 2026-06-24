package mst.local.mstsoftware.modules.users.services.impl;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.modules.users.resources.UserResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.services.BaseService;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    @Override
    public LoginResource login(LoginRequest request) {
        try {
            // String email = request.getEmail();
            // String passowrd = request.getPassword();
            String token = "MSTSoftware";
            UserResource user = new UserResource(1L, "phamhoangtuanqn@gmail.com");
            return new LoginResource(token, user);
        } catch (Exception e) {
            throw new RuntimeException("Có vấn đề xảy ra");
        }
    }

}
