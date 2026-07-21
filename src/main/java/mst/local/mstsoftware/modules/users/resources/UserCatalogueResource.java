package mst.local.mstsoftware.modules.users.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCatalogueResource(Long id, String name, Boolean publish) {
    public static UserCatalogueResource fromEntity(UserCatalogue entity) {
        return UserCatalogueResource.builder()
                .id(entity.getId())
                .name(entity.getName())
                .publish(entity.getPublish())
                .build();
    }
}
