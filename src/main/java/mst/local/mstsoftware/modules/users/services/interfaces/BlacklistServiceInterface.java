package mst.local.mstsoftware.modules.users.services.interfaces;

import java.util.List;

public interface BlacklistServiceInterface {
    public void blacklistToken(String token);

    public boolean isTokenBlackList(String token);

    public void blacklistAllUserTokens(Long userId, List<String> activeTokens);
}
