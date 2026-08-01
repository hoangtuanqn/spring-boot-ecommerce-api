package mst.local.mstsoftware.modules.product.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "Tên danh mục không được để trống!")
        @Size(max = 255, message = "Độ dài tên không được vượt quá 255 kí tự!")
        String name
) {
}
