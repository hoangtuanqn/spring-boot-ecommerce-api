package mst.local.mstsoftware.modules.order.requests;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequest(
        @Positive Integer quantity
) {
}