package mst.local.mstsoftware.modules.products.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import mst.local.mstsoftware.modules.products.services.interfaces.CategoryServiceInterface;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.BatchDeleteRequest;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {

    private final CategoryServiceInterface categoryService;

    @GetMapping
    public ResponseEntity<ApiResource<List<CategoryResource>>> index() {
        return ok(categoryService.list(), "Lấy danh sách danh mục sản phẩm thành công!");
    }

    @PostMapping
    public ResponseEntity<ApiResource<CategoryResource>> store(@Valid @RequestBody CreateCategoryRequest request) {
        return created(categoryService.store(request), "Thêm danh mục mới thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResource<CategoryResource>> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        return ok(categoryService.update(id, request), "Cập nhật danh mục thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResource<CategoryResource>> destroy(@PathVariable Long id) {
        return ok(categoryService.destroy(id), "Xóa danh mục thành công!");
    }

    @DeleteMapping("/batch-delete")
    public ResponseEntity<ApiResource<Void>> deleteMany(@Valid @RequestBody BatchDeleteRequest request) {
        categoryService.deleteMultiple(request.getIds());
        return ok(null, "Xóa tất cả category thành công!");
    }
}
