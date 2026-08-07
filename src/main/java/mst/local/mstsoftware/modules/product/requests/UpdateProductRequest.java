package mst.local.mstsoftware.modules.product.requests;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductRequest(
        @NotBlank(message = "Tên sản phẩm không được để trống!")
        @Size(max = 255, message = "Tên sản phẩm không được quá 255 kí tự!")
        String title,
        String description,

        @NotNull(message = "Giá tiền không được để trống!")
        @DecimalMin(value = "0", inclusive = false, message = "Giá tiền phải lớn hơn 0!")
        @DecimalMax(value = "100000000", message = "Số tiền phải nhỏ hơn 100 triệu!")
        BigDecimal price,

        @NotNull(message = "Category không được để trống")
        @Positive(message = "Category ID không hợp lệ!")
        Long categoryId,

        @NotNull(message = "Số lượng không được để trống!")
        @Min(value = 0, message = "Số lượng phải lớn hơn 0!")
        @Max(value = 10_000, message = "Số lượng phải nhỏ hơn 10.000!")
        Integer quantity,

        List<String> existingImages,
        List<MultipartFile> newImages

) {
}
