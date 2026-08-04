package mst.local.mstsoftware.modules.user.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.filters.JwtAuthFilter;
import mst.local.mstsoftware.helpers.CookieUtils;
import mst.local.mstsoftware.modules.user.requests.LoginRequest;
import mst.local.mstsoftware.modules.user.requests.RegisterRequest;
import mst.local.mstsoftware.modules.user.resources.AuthResult;
import mst.local.mstsoftware.modules.user.resources.LoginResource;
import mst.local.mstsoftware.modules.user.resources.RegisterResource;
import mst.local.mstsoftware.modules.user.services.interfaces.RefreshTokenServiceInterface;
import mst.local.mstsoftware.modules.user.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import mst.local.mstsoftware.services.interfaces.BlacklistServiceInterface;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final BlacklistServiceInterface blacklistService;
    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final RefreshTokenServiceInterface refreshTokenService;
    private final AuthConfig authConfig;


    @PostMapping("/login")
    public ResponseEntity<ApiResource<LoginResource>> login(@Valid @RequestBody LoginRequest request) {
        AuthResult auth = userService.authenticate(request);
        ResponseCookie refreshCookie = CookieUtils.buildRefreshTokenCookie(auth.refreshToken(), Duration.ofDays(authConfig.getRefreshTokenTTLDays()));

        LoginResource body = new LoginResource(auth.accessToken(), auth.user());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResource.success(body, "Đăng nhập tài khoản thành công!"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResource<RegisterResource>> login(@Valid @RequestBody RegisterRequest request) {
        AuthResult auth = userService.register(request);
        ResponseCookie refreshCookie = CookieUtils.buildRefreshTokenCookie(auth.refreshToken(), Duration.ofDays(authConfig.getRefreshTokenTTLDays()));

        RegisterResource body = new RegisterResource(auth.accessToken(), auth.user());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResource.success(body, "Đăng ký tài khoản thành công!"));
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResource<String>> logout(HttpServletRequest request, @CookieValue(name = "refresh_token", required = false) String refreshTokenRaw) {
        String token = (String) request.getAttribute(JwtAuthFilter.TOKEN_ATTRIBUTE);
        if (token == null || refreshTokenRaw == null) {
            throw new AuthenticationCredentialsNotFoundException("Không tìm thấy access token hoặc refresh token để đăng xuất!");
        }
        Map<String, Object> items = jwtService.extractRevoke(token);
        // revoked access token
        blacklistService.revoke((String) items.get("jti"), (Instant) items.get("expiresAt"));
        // revoked refresh token
        refreshTokenService.revokeToken(refreshTokenRaw);
        ResponseCookie clearCookie = CookieUtils.buildRefreshTokenCookie(null, Duration.ZERO);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(ApiResource.success(null, "Logout thành công!"));
    }
}
