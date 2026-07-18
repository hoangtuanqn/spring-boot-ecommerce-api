package mst.local.mstsoftware.helpers;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtils {
    public static ResponseCookie buildRefreshTokenCookie(String token, Duration maxAge) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
