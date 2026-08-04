package mst.local.mstsoftware.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mst.local.mstsoftware.config.AuthConfig;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService implements JwtServiceInterface {
    private final Long expirationTime;
    private final String issuer;
    private final SecretKey key;

    public JwtService(AuthConfig authConfig) {
        expirationTime = authConfig.getExpirationTime();
        issuer = authConfig.getIssuer();
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(authConfig.getSecretKey()));
    }

    // tạo ra jwt
    @Override
    public String generateToken(Long userId, String email, List<String> roles) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("roles", roles)
                .claim("jti", UUID.randomUUID().toString())
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(key) // tự động chọn thuật toán HS256, HS384, ... theo độ dài key của mình cho phù
                // hợp với đầu vào của từng thuật toán
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // check token còn hsd k
    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    @Override
    public String extractJti(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("jti", String.class);
    }

    @Override
    public Map<String, Object> extractRevoke(String token) {
        Claims claims = extractAllClaims(token);
        return Map.of("jti", claims.get("jti", String.class), "expiresAt", claims.getExpiration().toInstant());
    }

    @Override
    public List<RoleType> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        List<String> roleNames = claims.get("roles", List.class);
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptyList();
        }
        return roleNames.stream().map(RoleType::valueOf).toList();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


}
