package mst.local.mstsoftware.modules.users.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/user-catalogues")
public class UserCatalogueController {
    private final UserCatalogueServiceInterface userCatalogueService;
//    private final UserCa

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserCatalogueRequest request) {
        UserCatalogue userCatalogue = userCatalogueService.create(request);
        UserCatalogueResource userCatalogueResource = UserCatalogueResource.builder()
                .name(userCatalogue.getName())
                .publish(userCatalogue.getPublish())
                .build();
        return
                ResponseEntity.ok(ApiResource.success(userCatalogueResource, "Thêm user catalogue thành công!"));
    }
}
