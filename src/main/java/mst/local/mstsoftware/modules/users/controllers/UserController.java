package mst.local.mstsoftware.modules.users.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.repositories.UserRepository;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.modules.users.resources.UserResource;
import mst.local.mstsoftware.modules.users.services.impl.RefreshTokenService;
import mst.local.mstsoftware.resources.SuccessResource;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtServiceInterface jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthConfig authConfig;

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));

        UserResource userResource = UserResource.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        SuccessResource<UserResource> successResource = new SuccessResource<>("SUCCESS", userResource);
        return ResponseEntity.ok(successResource);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(@CookieValue("refresh_token") String rawRefreshToken) {
        var result = refreshTokenService.rotateToken(rawRefreshToken);
        User user = userRepository.findById(result.userId())
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));
        String newAccessToken = jwtService.generateToken(result.userId(), user.getEmail());
        ResponseCookie cookie = ResponseCookie.from("refresh_token", result.newRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(authConfig.getRefreshTokenTTLDays()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResource(newAccessToken, null));
    }
}
