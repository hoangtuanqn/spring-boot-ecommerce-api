package mst.local.mstsoftware.modules.order.requests;

import java.math.BigDecimal;
import java.util.List;

public record CartResource(
        List<CartItemResource> items,
        BigDecimal total
) {
}