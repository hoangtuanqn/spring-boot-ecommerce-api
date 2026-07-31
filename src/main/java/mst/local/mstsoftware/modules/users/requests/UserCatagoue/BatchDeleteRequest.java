package mst.local.mstsoftware.modules.users.requests.UserCatagoue;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record BatchDeleteRequest(
        @NotEmpty(message = "Không được để trống danh sách ids")
        List<
                @NotNull(message = "Id không được phép để null!")
                @Positive(message = "Id phải là số dương!")
                        Long> ids
) {
}