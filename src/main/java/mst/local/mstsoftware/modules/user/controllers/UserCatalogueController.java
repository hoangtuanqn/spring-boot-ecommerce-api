package mst.local.mstsoftware.modules.user.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.BatchDeleteRequest;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.user.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/user-catalogues")
public class UserCatalogueController extends BaseController {
    private final UserCatalogueServiceInterface userCatalogueService;

    @GetMapping("/all")
    public ResponseEntity<ApiResource<List<UserCatalogueResource>>> list() {
        return ok(userCatalogueService.list(), "Lấy danh sách user catalogues thành công!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> show(@PathVariable Long id) {
        return ok(userCatalogueService.findById(id), "Lấy chi tiết user catalogues thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> destroy(@PathVariable Long id) {
        return ok(userCatalogueService.destroy(id), "Xóa user catalogues thành công!");
    }

    @DeleteMapping("/batch-delete")
    public ResponseEntity<ApiResource<Void>> deleteMany(@Valid @RequestBody BatchDeleteRequest request) {
        userCatalogueService.deleteMultiple(request.ids());
        return ok(null, "Xóa tất cả user catalogues thành công!");
    }

    @GetMapping
    public ResponseEntity<ApiResource<Page<UserCatalogueResource>>> paginate(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Page<UserCatalogueResource> page = userCatalogueService.paginate(parameters);
        return ok(page, "Hiển thị danh sách user catalogues thành công!");
    }

    @PostMapping
    public ResponseEntity<ApiResource<UserCatalogueResource>> store(@Valid @RequestBody CreateUserCatalogueRequest request) {
        return created(userCatalogueService.store(request), "Thêm user catalogue thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> update(@PathVariable Long id, @Valid @RequestBody UpdateUserCatalogueRequest request) {
        return ok(userCatalogueService.update(id, request), "Cập nhật user catalogue thành công!");
    }
}
