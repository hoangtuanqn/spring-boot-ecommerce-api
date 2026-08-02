package mst.local.mstsoftware.modules.order.requests;

import java.math.BigDecimal;

public record CartItemResource(
        Long productId,
        String productTitle,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}