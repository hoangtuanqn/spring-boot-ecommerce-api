package mst.local.mstsoftware.modules.order.resources;

import lombok.Builder;
import mst.local.mstsoftware.modules.order.requests.CartItemResource;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartResource(
        List<CartItemResource> items,
        BigDecimal total
) {
}