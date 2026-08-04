package mst.local.mstsoftware.modules.user.resources;

public record RegisterResource(
        String accessToken,
        UserResource user
) {
}
