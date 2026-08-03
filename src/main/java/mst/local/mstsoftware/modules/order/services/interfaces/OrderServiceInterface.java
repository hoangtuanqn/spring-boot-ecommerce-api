package mst.local.mstsoftware.modules.order.services.interfaces;

import mst.local.mstsoftware.modules.order.requests.CancelOrderRequest;
import mst.local.mstsoftware.modules.order.requests.CheckoutRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.resources.OrderSummaryResource;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrderServiceInterface {
    public OrderResource checkout(Long userId, CheckoutRequest request);

    public OrderResource cancel(Long userId, Long orderId, CancelOrderRequest note);

    public Page<OrderSummaryResource> paginate(Long userId, Map<String, String[]> parameters);

    public OrderResource findByCode(Long userId, String code);
}
