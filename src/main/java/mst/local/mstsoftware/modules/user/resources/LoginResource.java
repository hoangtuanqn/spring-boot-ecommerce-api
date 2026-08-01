package mst.local.mstsoftware.modules.user.resources;

public record LoginResource(
        String accessToken,
        UserResource user) {
}
