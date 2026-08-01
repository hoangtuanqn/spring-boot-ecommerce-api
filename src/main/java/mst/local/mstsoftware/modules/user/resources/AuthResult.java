package mst.local.mstsoftware.modules.user.resources;

public record AuthResult(String accessToken, String refreshToken, UserResource user) {
}
