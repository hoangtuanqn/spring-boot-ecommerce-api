package mst.local.mstsoftware.modules.users.resources;

public record LoginResource(
        String accessToken,
        UserResource user) {
}
