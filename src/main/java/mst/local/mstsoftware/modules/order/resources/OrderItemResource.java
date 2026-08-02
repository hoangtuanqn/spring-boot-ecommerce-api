package mst.local.mstsoftware.modules.order.resources;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResource(
        Long id,
        Long productId,
        String productTitle,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}