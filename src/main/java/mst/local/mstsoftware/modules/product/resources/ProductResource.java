package mst.local.mstsoftware.modules.product.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResource(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Integer quantity,
        List<String> images,
        Instant createdAt
) {
}
