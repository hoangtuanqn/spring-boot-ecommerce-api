package mst.local.mstsoftware.modules.product.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.product.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.product.resources.ProductResource;
import mst.local.mstsoftware.modules.product.services.interfaces.ProductServiceInterface;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.BatchDeleteRequest;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {
    private final ProductServiceInterface productService;

    @GetMapping
    public ResponseEntity<ApiResource<Page<ProductResource>>> paginate(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Page<ProductResource> page = productService.paginate(parameters);
        return ok(page, "Hiển thị danh sách sản phẩm thành công!");
    }

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

    @DeleteMapping("/batch-delete")
    public ResponseEntity<ApiResource<Void>> deleteMany(@Valid @RequestBody BatchDeleteRequest request) {
        productService.deleteMultiple(request.ids());
        return ok(null, "Xóa tất cả sản phâm thành công!");
    }
}
