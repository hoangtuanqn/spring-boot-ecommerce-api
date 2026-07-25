package mst.local.mstsoftware.modules.users.requests.UserCatagoue;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchDeleteRequest {
    @NotEmpty(message = "Không được để trống danh sách ids")
    private List<Long> ids;
}
