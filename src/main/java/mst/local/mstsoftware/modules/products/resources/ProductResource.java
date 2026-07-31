package mst.local.mstsoftware.modules.products.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResource(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Integer quantity,
        Instant createdAt
) {
}
