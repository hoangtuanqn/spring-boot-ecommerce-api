package mst.local.mstsoftware.services.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtServiceInterface {
    public String generateToken(Long userId, String email);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public boolean isTokenExpired(String token);

    public String extractEmail(String token);

    public Long extractUserId(String token);

    public String extractJti(String token);

    public Map<String, Object> extractRevoke(String token);

    public Claims extractAllClaims(String token);

}
