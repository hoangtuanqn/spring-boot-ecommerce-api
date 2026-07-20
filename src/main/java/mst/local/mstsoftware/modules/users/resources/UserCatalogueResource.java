package mst.local.mstsoftware.modules.users.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCatalogueResource {
    private final Long id;
    private final String name;
    private final Boolean publish;

    public static UserCatalogueResource fromEntity(UserCatalogue entity) {
        return UserCatalogueResource.builder()
                .id(entity.getId())
                .name(entity.getName())
                .publish(entity.getPublish())
                .build();
    }
}
