package mst.local.mstsoftware.modules.users.resources;

public record AuthResult(String accessToken, String refreshToken, UserResource user) {
}
