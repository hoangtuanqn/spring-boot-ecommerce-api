package mst.local.mstsoftware.modules.users.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.config.JwtConfig;
import mst.local.mstsoftware.helpers.TokenHashUtil;
import mst.local.mstsoftware.modules.users.entities.RefreshToken;
import mst.local.mstsoftware.modules.users.repositories.RefreshTokenRepository;
import mst.local.mstsoftware.modules.users.services.interfaces.RefreshTokenServiceInterface;
import mst.local.mstsoftware.services.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@AllArgsConstructor
public class RefreshTokenService implements RefreshTokenServiceInterface {
    private JwtService jwtService;
    private RefreshTokenRepository repository;
    private JwtConfig JwtConfig;
    private TokenHashUtil utils;

    @Override
    public IssuedToken issueRefreshToken(Long userId) {
        String token = jwtService.generateRefreshTokenRaw();
        String tokenHash = utils.hash(token);
        RefreshToken entity = RefreshToken.builder()
                .tokenHash(tokenHash) // token đã hash
                .userId(userId)
                .expiryDate(Instant.now().plus(JwtConfig.getRefreshTokenTTLDays(), ChronoUnit.DAYS))
                .revoked(false)
                .build();
        repository.save(entity);

        return new IssuedToken(token, entity.getId());
    }

    @Override
    public RefreshResult rotateToken(String rawToken) {
        RefreshToken existing = repository.findByTokenHash(utils.hash(rawToken)).orElseThrow(() -> new BadCredentialsException("Refresh token không hợp lệ!"));
        this.checkIsRevoked(existing);
        this.checkExpiryDate(existing);
        existing.setRevoked(true);
        IssuedToken newToken = this.issueRefreshToken(existing.getUserId());
        existing.setReplacedByTokenId(newToken.tokenId());
        repository.save(existing);
        return new RefreshResult(existing.getUserId(), newToken.rawToken());
    }

    @Override
    @Transactional
    public void revokeToken(String rawToken) {
        RefreshToken existing = repository.findByTokenHash(utils.hash(rawToken)).orElseThrow(() -> new BadCredentialsException("Refresh token không hợp lệ."));
        this.checkIsRevoked(existing);
        this.checkExpiryDate(existing);
        existing.setRevoked(true);
        repository.save(existing);
    }

    private void checkIsRevoked(RefreshToken entity) {
        if (entity.isRevoked()) {
            // revoked hết tất cả những refresh token của người dùng
            repository.revokeAllRefreshTokenByUser(entity.getUserId());
            log.error("Phát hiện token bị đánh cắp, đã tiến hành revoke tất cả token của người dùng.");
            throw new SecurityException("Refresh token không hợp lệ.");
        }
    }

    private void checkExpiryDate(RefreshToken entity) {
        if (entity.getExpiryDate().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token không hợp lệ.");
        }
    }
}