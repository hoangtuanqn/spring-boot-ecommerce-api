package mst.local.mstsoftware.modules.users.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
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
public class UserCatalogueController {
    private final UserCatalogueServiceInterface userCatalogueService;

    @GetMapping("/all")
    public ResponseEntity<ApiResource<List<UserCatalogueResource>>> list() {
        return ResponseEntity.ok(
                ApiResource.success(userCatalogueService.list(), "Lấy danh sách user catalogues thành công!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> show(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResource.success(userCatalogueService.findById(id), "Lấy chi tiết user catalogues thành công!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> destroy(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResource.success(userCatalogueService.destroy(id), "Xóa user catalogues thành công!"));
    }

    @GetMapping
    public ResponseEntity<ApiResource<Page<UserCatalogueResource>>> paginate(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Page<UserCatalogueResource> page = userCatalogueService.paginate(parameters);
        return ResponseEntity.ok(ApiResource.success(page, "Hiển thị danh sách user catalogues thành công!"));
    }

    @PostMapping
    public ResponseEntity<ApiResource<UserCatalogueResource>> store(@Valid @RequestBody CreateUserCatalogueRequest request) {
        return
                ResponseEntity.ok(ApiResource.success(userCatalogueService.store(request), "Thêm user catalogue thành công!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> update(@PathVariable Long id, @Valid @RequestBody UpdateUserCatalogueRequest request) {
        return
                ResponseEntity.ok(ApiResource.success(userCatalogueService.update(id, request), "Cập nhật user catalogue thành công!"));
    }
}
