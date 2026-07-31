package mst.local.mstsoftware.modules.products.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResource(Long id, String name) {
}
