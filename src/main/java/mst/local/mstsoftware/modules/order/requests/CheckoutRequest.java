package mst.local.mstsoftware.modules.order.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "Vui lòng chọn ít nhất 1 sản phẩm để đặt hàng!")
        List<Long> productIds,
        String note
) {
}