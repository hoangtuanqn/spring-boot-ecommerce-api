package mst.local.mstsoftware.modules.order.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
        List<OrderItemRequest> items,
        String note
) {

    public record OrderItemRequest(
            @NotNull(message = "Product ID không được để trống")
            Long productId,

            @Positive(message = "Số lượng phải lớn hơn 0")
            Integer quantity
    ) {
    }
}