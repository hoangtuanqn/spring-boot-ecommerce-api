package mst.local.mstsoftware.modules.users.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.filters.JwtAuthFilter;
import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.AuthResult;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.services.interfaces.BlacklistServiceInterface;
import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final BlacklistServiceInterface blacklistService;
    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final RefreshTokenServiceInterface refreshTokenService;
    private final AuthConfig authConfig;


    @PostMapping("login")
    public ResponseEntity<LoginResource> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult auth = userService.authenticate(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", auth.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(authConfig.getRefreshTokenTTLDays()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        LoginResource body = new LoginResource(auth.accessToken(), auth.user());
        return ResponseEntity.ok(body);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @CookieValue(name = "refresh_token", required = false) String refreshTokenRaw) {
        String token = (String) request.getAttribute(JwtAuthFilter.TOKEN_ATTRIBUTE);
        if (token == null || refreshTokenRaw == null) {
            throw new AuthenticationCredentialsNotFoundException("Không tìm thấy access token hoặc refresh token để đăng xuất");
        }
        Map<String, Object> items = jwtService.extractRevoke(token);
        // revoked access token
        blacklistService.revoke((String) items.get("jti"), (Instant) items.get("expiresAt"));
        // revoked refresh token
        refreshTokenService.revokeToken(refreshTokenRaw);
        ResponseCookie clearCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }
}
