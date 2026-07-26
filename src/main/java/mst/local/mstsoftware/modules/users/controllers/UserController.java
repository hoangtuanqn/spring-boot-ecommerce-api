package mst.local.mstsoftware.modules.users.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.helpers.CookieUtils;
import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.resources.RefreshTokenResource;
import mst.local.mstsoftware.modules.users.resources.UserResource;
import mst.local.mstsoftware.modules.users.services.impl.RefreshTokenService;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
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
public class UserController extends BaseController {

    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthConfig authConfig;

    @GetMapping("me")
    public ResponseEntity<ApiResource<UserResource>> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));

        UserResource userResource = UserResource.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return ResponseEntity.ok(ApiResource.success(userResource, "Lấy thông tin thành công!"));
    }

    @PostMapping("refresh")
    public ResponseEntity<ApiResource<RefreshTokenResource>> refresh(@CookieValue("refresh_token") String rawRefreshToken) {
        var result = refreshTokenService.rotateToken(rawRefreshToken);
        User user = userService.findById(result.userId())
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));
        String newAccessToken = jwtService.generateToken(result.userId(), user.getEmail());

        ResponseCookie cookie = CookieUtils.buildRefreshTokenCookie(result.newRefreshToken(), Duration.ofDays(authConfig.getRefreshTokenTTLDays()));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResource.success(new RefreshTokenResource(newAccessToken), "Refresh token thành công!"));
    }
}