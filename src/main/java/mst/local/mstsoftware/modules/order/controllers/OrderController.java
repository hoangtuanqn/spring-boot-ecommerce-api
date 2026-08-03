package mst.local.mstsoftware.modules.order.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.controllers.BaseController;
import mst.local.mstsoftware.modules.order.requests.CancelOrderRequest;
import mst.local.mstsoftware.modules.order.requests.CheckoutRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.resources.OrderSummaryResource;
import mst.local.mstsoftware.modules.order.services.interfaces.OrderServiceInterface;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import mst.local.mstsoftware.resources.ApiResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {
    private final OrderServiceInterface orderService;

    @GetMapping
    public ResponseEntity<ApiResource<Page<OrderSummaryResource>>> myOrders(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, String[]> parameters = request.getParameterMap();
        Page<OrderSummaryResource> page = orderService.paginate(userDetails.getId(), parameters);
        return ok(page, "Lấy lịch sử đơn hàng thành công!");
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResource<OrderResource>> cancel(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CancelOrderRequest request) {
        return ok(orderService.cancel(userDetails.getId(), id, request), "Huỷ đơn hàng thành công!");
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResource<OrderResource>> checkout(@Valid @RequestBody CheckoutRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return created(orderService.checkout(userDetails.getId(), request), "Mua sản phẩm thành công!");
    }
}
