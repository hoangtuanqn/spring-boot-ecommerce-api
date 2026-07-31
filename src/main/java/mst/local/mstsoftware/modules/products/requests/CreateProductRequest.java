package mst.local.mstsoftware.modules.products.requests;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Tên sản phẩm không được để trống!")
        @Size(max = 255, message = "Tên sản phẩm không được quá 255 kí tự!")
        String title,
        String description,

        @Min(value = 0, message = "Giá tiền phải lớn hơn 0!")
        @Max(value = 100_000_000, message = "Số tiền phải nhỏ hơn 100 triệu!")
        BigDecimal price,

        @NotNull(message = "Category không được để trống")
        Long categoryId,

        @Min(value = 0, message = "Số lượng phải lớn hơn 0!")
        @Max(value = 10_000, message = "Số lượng phải nhỏ hơn 10.000!")
        Integer quantity
) {
}
