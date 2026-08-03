package mst.local.mstsoftware.modules.order.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartItemRequest(
        @NotNull(message = "Vui lòng chọn sản phẩm muốn thêm!") Long productId,

        @Positive(message = "Số luợng phải lớn hơn 0!") Integer quantity
) {
}