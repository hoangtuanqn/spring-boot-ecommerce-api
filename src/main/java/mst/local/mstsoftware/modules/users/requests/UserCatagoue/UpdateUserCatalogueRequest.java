package mst.local.mstsoftware.modules.users.requests.UserCatagoue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserCatalogueRequest(
        @NotBlank(message = "Tên nhóm không được để trống!")
        String name,

        @NotNull(message = "Trạng thái không được để trống!")
        Integer publish
) {
}
