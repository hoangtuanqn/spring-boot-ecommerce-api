package mst.local.mstsoftware.modules.products.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.products.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.products.resources.ProductResource;
import mst.local.mstsoftware.modules.products.services.interfaces.ProductServiceInterface;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {
    private final ProductServiceInterface productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResource<ProductResource>> show(@PathVariable Long id) {
        return ok(productService.findById(id), "Lấy chi tiết sản phẩm thành công!");
    }

    @PostMapping
    public ResponseEntity<ApiResource<ProductResource>> store(@Valid @RequestBody CreateProductRequest request) {
        return created(productService.store(request), "Thêm sản phẩm mới thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResource<ProductResource>> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return ok(productService.update(id, request), "Cập nhật sản phẩm thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResource<ProductResource>> destroy(@PathVariable Long id) {
        return ok(productService.destroy(id), "Xóa sản phẩm thành công!");
    }
}
