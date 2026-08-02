package mst.local.mstsoftware.modules.order.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.order.requests.AddCartItemRequest;
import mst.local.mstsoftware.modules.order.requests.CartResource;
import mst.local.mstsoftware.modules.order.requests.UpdateCartItemRequest;
import mst.local.mstsoftware.modules.order.services.interfaces.CartServiceInterface;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.BatchDeleteRequest;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/cart")
public class CartController extends BaseController {
    private final CartServiceInterface cartService;

    @GetMapping
    public ResponseEntity<ApiResource<CartResource>> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(cartService.getCart(userDetails.getId()), "Lấy giỏ hàng thành công!");
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResource<CartResource>> addItem(@Valid @RequestBody AddCartItemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(cartService.addItem(userDetails.getId(), request), "Thêm vào giỏ hàng thành công!");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResource<Void>> removeItem(@PathVariable Long productId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.removeItem(userDetails.getId(), productId);
        return ok(null, "Xoá sản phẩm khỏi giỏ hàng thành công!");
    }

    @DeleteMapping("/items/batch-delete")
    public ResponseEntity<ApiResource<Void>> removeItemMany(@Valid @RequestBody BatchDeleteRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.removeItemMany(userDetails.getId(), request.ids());
        return ok(null, "Xoá sản phẩm khỏi giỏ hàng thành công!");
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<ApiResource<CartResource>> updateItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("vô đây rồi");
        return ok(cartService.updateItem(userDetails.getId(), productId, request), "Cập nhật giỏ hàng thành công!");
    }
}
