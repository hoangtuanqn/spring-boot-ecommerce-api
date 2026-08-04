package mst.local.mstsoftware.modules.user.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.entities.UserRole;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;
import mst.local.mstsoftware.modules.user.requests.LoginRequest;
import mst.local.mstsoftware.modules.user.resources.AuthResult;
import mst.local.mstsoftware.modules.user.resources.UserResource;
import mst.local.mstsoftware.modules.user.services.interfaces.RefreshTokenServiceInterface.IssuedToken;
import mst.local.mstsoftware.modules.user.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService extends BaseService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final JwtServiceInterface jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserSessionCache userSessionCache;
    private final AuthConfig authConfig;

    @Override
    public AuthResult authenticate(LoginRequest request) {
        String email = request.email();
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new BadCredentialsException("Email hoặc mật khẩu không chính xác!"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Email hoặc mật khẩu không chính xác!");
        }

        Set<RoleType> roles = user.getUserRoles().stream()
                .filter(UserRole::isActive)
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());


        String accessToken = jwtService.generateToken(user.getId());

        Duration ttl = Duration.ofMillis(authConfig.getExpirationTime());
        userSessionCache.set(user.getId(), user.getEmail(), roles, ttl);

        IssuedToken refreshToken = refreshTokenService.issueRefreshToken(user.getId());
        UserResource userResource = UserResource.builder()
                .id(user.getId())
                .email(email)
                .name(user.getName())
                .phone(user.getPhone())
                .build();
        return new AuthResult(accessToken, refreshToken.rawToken(), userResource);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResource getMe(Long userId) {
        var user = findOrThrow(findById(userId), "Người dùng này không tồn tại!");
        return UserResource.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
