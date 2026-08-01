package mst.local.mstsoftware.modules.product.requests;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Tên sản phẩm không được để trống!")
        @Size(max = 255, message = "Tên sản phẩm không được quá 255 kí tự!")
        String title,
        String description,

        @NotNull(message = "Giá tiền không được để trống!")
        @DecimalMin(value = "0", message = "Giá tiền phải lớn hơn hoặc bằng 0!")
        @DecimalMax(value = "100000000", message = "Số tiền phải nhỏ hơn hoặc bằng 100 triệu!")
        BigDecimal price,

        @NotNull(message = "Category không được để trống")
        @Positive(message = "Category ID không hợp lệ!")
        Long categoryId,

        @NotNull(message = "Số lượng không được để trống!")
        @Min(value = 0, message = "Số lượng phải lớn hơn 0!")
        @Max(value = 10_000, message = "Số lượng phải nhỏ hơn 10.000!")
        Integer quantity
) {
}
