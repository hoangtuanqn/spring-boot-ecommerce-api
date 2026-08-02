package mst.local.mstsoftware.modules.order.requests;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResource(
        Long productId,
        String productTitle,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}