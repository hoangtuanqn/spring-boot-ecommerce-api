package mst.local.mstsoftware.modules.order.requests;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartResource(
        List<CartItemResource> items,
        BigDecimal total
) {
}