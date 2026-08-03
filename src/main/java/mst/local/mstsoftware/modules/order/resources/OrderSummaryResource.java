package mst.local.mstsoftware.modules.order.resources;

import mst.local.mstsoftware.modules.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderSummaryResource(
        Long id,
        OrderStatus status,
        BigDecimal totalPrice,
        String note,
        int itemCount,
        Instant createdAt
) {
}
