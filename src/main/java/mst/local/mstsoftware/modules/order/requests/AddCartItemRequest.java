package mst.local.mstsoftware.modules.order.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartItemRequest(
        @NotNull Long productId,
        @Positive Integer quantity
) {
}