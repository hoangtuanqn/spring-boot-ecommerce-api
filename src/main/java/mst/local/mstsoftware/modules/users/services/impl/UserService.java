package mst.local.mstsoftware.modules.users.services.impl;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import mst.local.mstsoftware.modules.users.dtos.LoginRequest;
import mst.local.mstsoftware.modules.users.dtos.LoginResponse;
import mst.local.mstsoftware.modules.users.dtos.UserDTO;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.services.BaseService;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // String email = request.getEmail();
            // String passowrd = request.getPassword();
            String token = "MSTSoftware";
            UserDTO user = new UserDTO(1L, "phamhoangtuanqn@gmail.com");
            return new LoginResponse(token, user);
        } catch (Exception e) {
            throw new RuntimeException("Có vấn đề xảy ra");
        }
    }

}
