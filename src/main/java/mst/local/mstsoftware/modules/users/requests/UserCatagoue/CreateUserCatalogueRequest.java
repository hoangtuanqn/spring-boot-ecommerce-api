package mst.local.mstsoftware.modules.users.requests.UserCatagoue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserCatalogueRequest {
    @NotBlank(message = "Tên nhóm không được để trống!")
    private String name;

    @NotNull(message = "Trạng thái không được để trống!")
    private Integer publish;

}
