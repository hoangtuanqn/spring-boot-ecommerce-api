package mst.local.mstsoftware.modules.user.requests.UserCatagoue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserCatalogueRequest(
        @NotBlank(message = "Tên nhóm không được để trống!")
        String name,

        @NotNull(message = "Trạng thái không được để trống!")
        Integer publish
) {


}
