package mst.local.mstsoftware.modules.users.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.helpers.JwtAuthFilter;
import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.AuthResult;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.modules.users.services.interfaces.BlacklistServiceInterface;
import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final BlacklistServiceInterface blacklistService;
    private final UserServiceInterface userService;
    private final RefreshTokenServiceInterface refreshTokenService;


    @PostMapping("login")
    public ResponseEntity<LoginResource> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResult auth = userService.authenticate(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", auth.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/") // chỉ gửi kèm khi gọi đúng endpoint refresh
                .maxAge(Duration.ofDays(14))
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
        // revoked access token
        blacklistService.blacklistToken(token);
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
