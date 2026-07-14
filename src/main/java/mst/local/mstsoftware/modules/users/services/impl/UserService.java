package mst.local.mstsoftware.modules.users.services.impl;

import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.repositories.UserRepository;
import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.AuthResult;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.modules.users.resources.UserResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.services.BaseService;
import mst.local.mstsoftware.services.JwtService;
import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface.IssuedToken;

@Service
@AllArgsConstructor
public class UserService extends BaseService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResult authenticate(LoginRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email hoặc mật khẩu không chính xác!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email hoặc mật khẩu không chính xác!");
        }
        String accessToken = jwtService.generateToken(user.getId(), email);
        IssuedToken refreshToken = refreshTokenService.issueRefreshToken(user.getId());
        UserResource userResource = new UserResource(user.getId(), email, user.getName(), user.getPhone());
        return new AuthResult(accessToken, refreshToken.rawToken(), userResource);
    }

}
