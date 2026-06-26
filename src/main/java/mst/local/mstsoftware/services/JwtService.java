package mst.local.mstsoftware.services;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mst.local.mstsoftware.config.JwtConfig;

@Service
public class JwtService {
    private final Long expirationTime;
    private final String issuer;
    private final SecretKey key;

    public JwtService(JwtConfig jwtConfig) {
        expirationTime = jwtConfig.getExpirationTime();
        issuer = jwtConfig.getIssuer();
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecretKey()));
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(key) // tự động chọn thuật toán HS256, HS384, ... theo độ dài key của mình cho phù
                               // hợp với đầu vào của từng thuật toán
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
