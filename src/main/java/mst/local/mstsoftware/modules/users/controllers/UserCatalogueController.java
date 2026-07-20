package mst.local.mstsoftware.modules.users.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/user-catalogues")
public class UserCatalogueController {
    private final UserCatalogueServiceInterface userCatalogueService;
//    private final UserCa

    @PostMapping
    public ResponseEntity<ApiResource<UserCatalogueResource>> create(@Valid @RequestBody CreateUserCatalogueRequest request) {
        return
                ResponseEntity.ok(ApiResource.success(userCatalogueService.create(request), "Thêm user catalogue thành công!"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResource<UserCatalogueResource>> update(@PathVariable Long id, @Valid @RequestBody UpdateUserCatalogueRequest request) {
        return
                ResponseEntity.ok(ApiResource.success(userCatalogueService.update(id, request), "Cập nhật user catalogue thành công!"));
    }
}
