package mst.local.mstsoftware.modules.order.controllers;

import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.order.requests.CartResource;
import mst.local.mstsoftware.modules.order.services.interfaces.CartServiceInterface;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController extends BaseController {
    private final CartServiceInterface cartService;

    @GetMapping
    public ResponseEntity<ApiResource<CartResource>> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(cartService.getCart(userDetails.getId()), "Lấy giỏ hàng thành công!");
    }

}
