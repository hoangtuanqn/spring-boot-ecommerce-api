package mst.local.mstsoftware.modules.products.controllers;

import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import mst.local.mstsoftware.modules.products.services.interfaces.CategoryServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
