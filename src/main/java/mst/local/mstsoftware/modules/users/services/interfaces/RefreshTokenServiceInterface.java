package mst.local.mstsoftware.modules.users.services.interfaces;

public interface RefreshTokenServiceInterface {
    public String issueRefreshToken(Long userId);

    public RefreshResult rotateToken(String rawToken);

    public record RefreshResult(String userEmail, String newRefreshToken) {
    }
}
