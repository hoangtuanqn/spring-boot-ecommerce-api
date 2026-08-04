package mst.local.mstsoftware.services.interfaces;

import io.jsonwebtoken.Claims;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;

import java.util.Map;

public interface JwtServiceInterface {
    public String generateToken(Long userId);

    public boolean isTokenValid(String token, CustomUserDetails userDetails);

    public boolean isTokenExpired(String token);

    public Long extractSubject(String token);

    public String extractJti(String token);

    public Map<String, Object> extractRevoke(String token);

    public Claims extractAllClaims(String token);

}
