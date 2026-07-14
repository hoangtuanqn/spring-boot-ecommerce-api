package mst.local.mstsoftware.modules.users.services.interfaces;

public interface RefreshTokenServiceInterface {
    public IssuedToken issueRefreshToken(Long userId);

    public RefreshResult rotateToken(String rawToken);

    public record RefreshResult(Long userId, String newRefreshToken) {
    }
    public record IssuedToken(String rawToken, Long tokenId) {}
}
