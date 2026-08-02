package mst.local.mstsoftware.modules.order.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.order.requests.CreateOrderRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.services.interfaces.OrderServiceInterface;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {
    private final OrderServiceInterface orderService;

    @PostMapping
    public ResponseEntity<ApiResource<OrderResource>> store(@Valid @RequestBody CreateOrderRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return created(orderService.store(userDetails.getId(), request), "Mua sản phẩm thành công!");
    }
}
