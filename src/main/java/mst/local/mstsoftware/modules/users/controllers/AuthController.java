package mst.local.mstsoftware.modules.users.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import mst.local.mstsoftware.helpers.JwtAuthFilter;
import mst.local.mstsoftware.modules.users.requests.LoginRequest;
import mst.local.mstsoftware.modules.users.resources.LoginResource;
import mst.local.mstsoftware.modules.users.services.impl.BlacklistService;
import mst.local.mstsoftware.modules.users.services.interfaces.UserServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final BlacklistService blacklistService;
    private final UserServiceInterface userService;

    public AuthController(UserServiceInterface userService, BlacklistService blacklistService) {
        this.userService = userService;
        this.blacklistService = blacklistService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResource> login(@Valid @RequestBody LoginRequest request, HttpServletResponse resonse) {
        LoginResource auth = userService.authenticate(request);
        Cookie cookie = new Cookie("token", auth.getToken());
        cookie.setMaxAge(14 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        resonse.setHeader("Access-Control-Allow-Credentials", "true");
        resonse.addCookie(cookie);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute(JwtAuthFilter.TOKEN_ATTRIBUTE);
        if (token == null) {
            throw new AuthenticationCredentialsNotFoundException("Không tìm thấy token để đăng xuất");
        }
        blacklistService.blacklistToken(token);
        return ResponseEntity.noContent().build();
    }
}
