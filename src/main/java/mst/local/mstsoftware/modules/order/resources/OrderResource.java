package mst.local.mstsoftware.modules.order.resources;

import lombok.Builder;
import mst.local.mstsoftware.modules.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record OrderResource(
        Long id,
        OrderStatus status,
        BigDecimal totalPrice,
        String note,
        List<OrderItemResource> items,
        Instant createdAt
) {
}
