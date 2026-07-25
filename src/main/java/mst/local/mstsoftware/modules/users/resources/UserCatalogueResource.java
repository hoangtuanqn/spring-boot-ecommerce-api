package mst.local.mstsoftware.modules.users.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCatalogueResource(Long id, String name, Integer publish) {
}
